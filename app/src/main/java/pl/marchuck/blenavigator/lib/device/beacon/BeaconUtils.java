package pl.marchuck.blenavigator.lib.device.beacon;

import pl.marchuck.blenavigator.lib.device.BluetoothLeDevice;
import pl.marchuck.blenavigator.lib.device.adrecord.AdRecord;
import pl.marchuck.blenavigator.lib.device.beacon.ibeacon.IBeaconConstants;
import pl.marchuck.blenavigator.lib.util.ByteUtils;

/**
 *
 */
public final class BeaconUtils {

    private BeaconUtils(){
        // TO AVOID INSTANTIATION
    }

    /**
     * Ascertains whether a Manufacturer Data byte array belongs to a known Beacon type;
     *
     * @param manufacturerData a Bluetooth LE device's raw manufacturerData.
     * @return the {@link BeaconType}
     */
    public static BeaconType getBeaconType(final byte[] manufacturerData) {
        if (manufacturerData == null || manufacturerData.length == 0) {
            return BeaconType.NOT_A_BEACON;
        }

        if(isIBeacon(manufacturerData)){
            return BeaconType.IBEACON;
        } else {
            return BeaconType.NOT_A_BEACON;
        }
    }

    /**
     * Ascertains whether a {@link uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice} is an iBeacon;
     *
     * @param device a {@link uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice} device.
     * @return the {@link BeaconType}
     */
    public static BeaconType getBeaconType(final BluetoothLeDevice device) {
        final int key = AdRecord.TYPE_MANUFACTURER_SPECIFIC_DATA;
        return getBeaconType(device.getAdRecordStore().getRecordDataAsString(key).getBytes());
    }

    private static boolean isIBeacon(final byte[] manufacturerData){
        // An iBeacon record must be at least 25 chars long
        if (!(manufacturerData.length >= 25)) {
            return false;
        }

        if (ByteUtils.doesArrayBeginWith(manufacturerData, IBeaconConstants.MANUFACTURER_DATA_IBEACON_PREFIX)) {
            return true;
        }

        return false;
    }
}