package pl.marchuck.blenavigator;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BluetoothService extends Service {
    public static final String TAG = BluetoothService.class.getSimpleName();
    private RxBleClient rxBleClient;
    private final IBinder myBinder = new MyLocalBinder();
    private rx.Subscription scanSubscription;

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

    public class MyLocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    public BluetoothService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        rxBleClient = RxBleClient.create(this);

        return Service.START_STICKY;
    }

    /**
     * start scan for beacons
     */
    public void startScan() {
        Log.d(TAG, "startScan: ");
        if (rxBleClient==null) RxBleClient.create(this);
        scanSubscription = rxBleClient.scanBleDevices()
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<RxBleScanResult>() {
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
                if (device != null)
                    Log.d(TAG, "onNext: " + device.getName() + ", " + device.getMacAddress());
            }
        });
    }

    public void stopScan() {
        Log.d(TAG, "stopScan: ");
        if (scanSubscription != null) {
            scanSubscription.unsubscribe();
            scanSubscription = null;
        }
    }
}
