package pl.marchuck.blenavigator.ble;

import android.support.annotation.IntDef;
import android.util.Log;

import com.polidea.rxandroidble.RxBleScanResult;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.marchuck.blenavigator.utils.AdRecordUtils;


/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public class BleMeasure {
    public static final String TAG = BleMeasure.class.getSimpleName();


    public static double calculateAccuracy(RxBleScanResult result) {
        AdRecordStore adRecordStore = new AdRecordStore(AdRecordUtils.parseScanRecordAsSparseArray(result.getScanRecord()));
        AdRecord record = adRecordStore.getRecord(AdRecord.TYPE_MANUFACTURER_SPECIFIC_DATA);
        if (record == null) {
            Log.e(TAG, "nullable ad record");
            return NOT_A_BEACON;
        }
        byte[] data = record.getData();
        int rssi = result.getRssi();
        if (data.length < 24) {
            Log.e(TAG, "too short data!!!");
            return TOO_SHORT_DATA;
        }
        IBeaconData ibeaconData = new IBeaconData(data);
        Log.d("BleMeasure", "getDistance: " + ibeaconData.toString());
        int txPower = ibeaconData.getCalibratedTxPower();
        return calculateAccuracy(txPower, rssi);
    }

    @Distance
    public static int getDistance(RxBleScanResult result) {
        double accuracy = calculateAccuracy(result);
        return getDistance(accuracy);
    }


    private static final double DISTANCE_THRESHOLD_WTF = 0.0;
    private static final double DISTANCE_THRESHOLD_IMMEDIATE = 0.5;
    private static final double DISTANCE_THRESHOLD_NEAR = 3.0;

    public static final int NOT_A_BEACON = 0x06;
    public static final int TOO_SHORT_DATA = 0x05;
    public static final int UNKNOWN = 0x04;
    public static final int FAR = 0x03;
    public static final int NEAR = 0x02;
    public static final int IMMEDIATE = 0x01;

    public static String readableDistance(@Distance int distance) {
        return distance == NEAR ? "NEAR" : distance == FAR ? "FAR" : distance == IMMEDIATE ? "IMMEDIATE" : "UNKNOWN";
    }

    @IntDef({FAR, NEAR, IMMEDIATE, UNKNOWN, TOO_SHORT_DATA, NOT_A_BEACON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Distance {
    }

    /**
     * Calculates the accuracy of an RSSI reading.
     * <p>
     * The code was taken from <a href="http://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing" /a>
     *
     * @param txPower the calibrated TX power of an iBeacon
     * @param rssi    the RSSI value of the iBeacon
     * @return the calculated Accuracy
     */
    public static double calculateAccuracy(final int txPower, final double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        final double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

    @Distance
    public static int getDistance(final double accuracy) {
        if (accuracy < DISTANCE_THRESHOLD_WTF) {
            return UNKNOWN;
        }

        if (accuracy < DISTANCE_THRESHOLD_IMMEDIATE) {
            return IMMEDIATE;
        }

        if (accuracy < DISTANCE_THRESHOLD_NEAR) {
            return NEAR;
        }
        return FAR;
    }
}
