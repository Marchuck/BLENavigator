package pl.marchuck.blenavigator.ble;

import com.polidea.rxandroidble.RxBleDevice;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public class DistBeacon {
    private final RxBleDevice device;
    private final double accuracy;

    public RxBleDevice getDevice() {
        return device;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public DistBeacon(RxBleDevice device, double accuracy) {
        this.device = device;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "mac: " + device.getMacAddress() + "name: , " + device.getName() +
                ", accuracy: " + accuracy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistBeacon that = (DistBeacon) o;

        return (device != null ? device.equals(that.device) : that.device == null);

    }

    @Override
    public int hashCode() {
        int result;
        result = device != null ? device.hashCode() : 0;
        return result;
    }
}
