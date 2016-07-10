package pl.marchuck.blenavigator.presenter;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public interface ScanCallbacks {
    void onStartScan();
    void onStopScan();
    void updateScanResults(String s);
}
