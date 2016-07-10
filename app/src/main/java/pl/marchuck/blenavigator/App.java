package pl.marchuck.blenavigator;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    public static void enableBt(Context ctx) {
        BluetoothManager btManager = (BluetoothManager) ctx.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = btManager.getAdapter();
        if (!adapter.isEnabled()) {
            adapter.enable();
            adapter.enable();
            adapter.enable();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        Rx.enableBluetooth(this).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "enabling bluetooth completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }
}
