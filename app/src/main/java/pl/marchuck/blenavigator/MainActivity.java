package pl.marchuck.blenavigator;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;
import com.skyfishjy.library.RippleBackground;

import java.util.HashSet;
import java.util.Set;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    BluetoothService myService;
    boolean isBound = false, isStarted = false;
    private Set<String> results = new HashSet<>();
    private RxBleClient client;

    private ServiceConnection myBluetoothConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected: " + Thread.currentThread().getName());

            BluetoothService.MyLocalBinder binder = (BluetoothService.MyLocalBinder) service;
            myService = binder.getService();
            isBound = true;
            Log.d(TAG, "onServiceConnected: " + (myService != null));
            myService.startScan();
            isStarted = true;
//            fab2.setVisibility(View.VISIBLE);
//            fab2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick: " + Thread.currentThread().getName());
//                    if (isStarted) myService.stopScan();
//                    else myService.startScan();
//                }
//            });
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };
    FloatingActionButton fab;
    private boolean isScanning;
    RippleBackground rippleBackground;
    TextView textView;
    rx.Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = RxBleClient.create(this);
        rippleBackground = (RippleBackground) findViewById(R.id.content);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        textView = (TextView) findViewById(R.id.textView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isScanning) {
                    fab.setImageResource(R.drawable.bluetooth_connect);
                    rippleBackground.startRippleAnimation();
                    Log.d(TAG, "isScanning not bound: binding now ");
                    //Intent intent = new Intent(MainActivity.this, BluetoothService.class);
                    //bindService(intent, myBluetoothConnection, Context.BIND_AUTO_CREATE);
                    App.enableBt(MainActivity.this);
                    subscription = client.scanBleDevices().filter(new Func1<RxBleScanResult, Boolean>() {
                        @Override
                        public Boolean call(RxBleScanResult rxBleScanResult) {
                            int accuracy = BleMeasure.getDistance(rxBleScanResult);
                            Log.d(TAG, "detected beacon: " + BleMeasure.readableDistance(accuracy));
                            boolean result = accuracy == BleMeasure.NEAR || accuracy == BleMeasure.IMMEDIATE;//|| accuracy == BleMeasure.TOO_SHORT_DATA || accuracy == BleMeasure.NOT_A_BEACON;
                            return result;
                        }
                    }).subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<RxBleScanResult>() {
                                @Override
                                public void onCompleted() {
                                    Log.d(TAG, "onCompleted: ");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "onError: ", e);
                                }

                                @Override
                                public void onNext(RxBleScanResult rxBleScanResult) {
                                    RxBleDevice device = rxBleScanResult.getBleDevice();
                                    if (device != null) {
                                        String optName = device.getName();
                                        optName = optName == null ? "" : ", name: " + optName;
                                        String log = device.getMacAddress() + optName;
                                        Log.d(TAG, "onNext: " + log);

                                        results.add(log);
                                        textView.setText(newResults());
                                    }
                                }
                            });

                } else {
                    Log.d(TAG, "isScanning bound, unbinding: ");

                    rippleBackground.animate()
                            .alpha(0)
                            .setDuration(1000)
                            .start();
                    WeakHandler wh = new WeakHandler();
                    wh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //rippleBackground.setAlpha(1);
                            rippleBackground.stopRippleAnimation();
                            fab.setImageResource(R.drawable.bluetooth);
                            rippleBackground.animate()
                                    .alpha(1)
                                    .setDuration(300)
                                    .start();
                            if (subscription != null) subscription.unsubscribe();
                        }
                    }, 1000);

                    //unbindService(myBluetoothConnection);
                }
                isScanning = !isScanning;
            }
        });
    }

    private String newResults() {
        StringBuilder sb = new StringBuilder();
        for (String sc : results) {
            sb.append(sc).append('\n');
        }
        return sb.toString();
    }
}
