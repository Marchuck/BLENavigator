package pl.marchuck.blenavigator;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;
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
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        enableBt(this);
    }
}
