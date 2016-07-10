package pl.marchuck.blenavigator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import pl.marchuck.blenavigator.ble.BluetoothService;
import pl.marchuck.blenavigator.presenter.ScanCallbacks;
import pl.marchuck.blenavigator.presenter.ScanPresenter;
import pl.marchuck.blenavigator.common.WeakHandler;

public class MainActivity extends AppCompatActivity implements ScanCallbacks {
    public static final String TAG = MainActivity.class.getSimpleName();

    private ScanPresenter scanPresenter = new ScanPresenter();
    private ServiceConnection myBluetoothConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected: " + Thread.currentThread().getName());

            BluetoothService.MyLocalBinder binder = (BluetoothService.MyLocalBinder) service;
            BluetoothService myService = binder.getService();
            scanPresenter.setScanService(myService);
            Log.d(TAG, "onServiceConnected: " + (myService != null));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanPresenter.onFabClick();
                }
            });
        }

        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };
    private FloatingActionButton fab;
    private RippleBackground rippleBackground;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanPresenter.setView(this);
        injectViews();
        setupService();
    }

    private void setupService() {
        Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        startService(intent);
        bindService(intent, myBluetoothConnection, Context.BIND_AUTO_CREATE);
    }

    private void injectViews() {
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void onStartScan() {
        fab.setImageResource(R.drawable.bluetooth_connect);
        rippleBackground.startRippleAnimation();
    }

    @Override
    public void onStopScan() {
        rippleBackground.animate()
                .alpha(0)
                .setDuration(600)
                .start();
        WeakHandler wh = new WeakHandler();
        wh.postDelayed(new Runnable() {
            @Override
            public void run() {
                rippleBackground.stopRippleAnimation();
                fab.setImageResource(R.drawable.bluetooth);
                rippleBackground.animate()
                        .alpha(1)
                        .setDuration(300)
                        .start();
            }
        }, 600);
    }

    @Override
    public void updateScanResults(String results) {
        textView.setText(results);
    }

    @Override
    protected void onDestroy() {
        unbindService(myBluetoothConnection);
        super.onDestroy();
    }
}
