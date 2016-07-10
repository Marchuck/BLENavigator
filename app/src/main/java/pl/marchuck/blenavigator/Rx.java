package pl.marchuck.blenavigator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
@Deprecated
public class Rx {

    public static rx.Observable<Boolean> enableBluetooth(final Context ctx) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                BluetoothManager btManager = (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothAdapter adapter = btManager.getAdapter();
                return Observable.just(adapter.isEnabled() || adapter.enable());
            }
        }).subscribeOn(Schedulers.trampoline());
    }
}
