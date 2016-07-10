package pl.marchuck.blenavigator.ble;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleScanResult;

import java.util.concurrent.TimeUnit;

import pl.marchuck.blenavigator.App;
import pl.marchuck.blenavigator.common.DelayedAction;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
       public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    public BluetoothService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    /**
     * start scan for beacons
     */
    public void startScan(Subscriber<DistBeacon> subscriber, DelayedAction delayedAction) {
        Log.d(TAG, "startScan: ");
        if (rxBleClient == null) rxBleClient = RxBleClient.create(this);
        App.enableBt(this);
        scanSubscription = rxBleClient.scanBleDevices()
                .map(new Func1<RxBleScanResult, DistBeacon>() {
                    @Override
                    public DistBeacon call(RxBleScanResult result) {
                        return new DistBeacon(result.getBleDevice(), BleMeasure.calculateAccuracy(result));
                    }
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        Observable.timer(delayedAction.getTime(), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delayedAction, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "error: ", throwable);
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
