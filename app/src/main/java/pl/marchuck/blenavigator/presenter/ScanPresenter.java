package pl.marchuck.blenavigator.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.marchuck.blenavigator.ble.BleMeasure;
import pl.marchuck.blenavigator.ble.BluetoothService;
import pl.marchuck.blenavigator.ble.DistBeacon;
import pl.marchuck.blenavigator.common.DelayedAction;
import pl.marchuck.blenavigator.common.PragmaticSubscriber;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public class ScanPresenter {
    public static final String TAG = ScanPresenter.class.getSimpleName();
    private ScanCallbacks uiCallbacks;
    private boolean isScanRunning;
    private BluetoothService scanService;
    private List<DistBeacon> beacons = new ArrayList<>();

    public void setView(ScanCallbacks callbacks) {
        this.uiCallbacks = callbacks;
    }

    public void onStartScan() {
        scanService.startScan(new PragmaticSubscriber<DistBeacon>(TAG) {
            @Override
            public void onNext(DistBeacon beacon) {
                Log.d(TAG, "onNext: ");
                updateScanResults(beacon);
                uiCallbacks.updateScanResults(newResults());
            }
        }, new DelayedAction(10) {
            @Override
            public void call(Long aLong) {
                if (isScanRunning) stopScan();
            }
        });
        uiCallbacks.onStartScan();
    }

    private void updateScanResults(DistBeacon beacon) {
        boolean exists = false;
        for (int j = 0; j < beacons.size(); j++) {
            DistBeacon b = beacons.get(j);
            if (b.getDevice().getMacAddress().equalsIgnoreCase(beacon
                    .getDevice().getMacAddress()) ||
                    (b.getDevice().getName() == null && beacon.getDevice().getName() == null)) {
                beacons.set(j, beacon);
                exists = true;
            }
        }
        if (!exists) beacons.add(beacon);

    }

    private void stopScan() {
        scanService.stopScan();
        uiCallbacks.onStopScan();
    }

    private String newResults() {
        StringBuilder sb = new StringBuilder();
        Collections.sort(beacons, new Comparator<DistBeacon>() {
            @Override
            public int compare(DistBeacon lhs, DistBeacon rhs) {
                return Double.compare(lhs.getAccuracy(), rhs.getAccuracy());
            }
        });
        for (DistBeacon sc : beacons) {
            sb.append(sc.getDevice().getName())
                    .append(' ').append(BleMeasure
                    .readableDistance(BleMeasure.getDistance(sc.getAccuracy())))
                    .append(' ').append(sc.getAccuracy())
                    .append('\n');
        }
        return sb.toString();
    }

    public void setScanService(BluetoothService scanService) {
        this.scanService = scanService;
    }

    public void onFabClick() {
        if (isScanRunning) {
            stopScan();
        } else {
            onStartScan();
        }
        isScanRunning = !isScanRunning;
    }
}
