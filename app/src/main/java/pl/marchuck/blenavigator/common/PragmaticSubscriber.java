package pl.marchuck.blenavigator.common;

import android.util.Log;

import rx.Subscriber;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public abstract class PragmaticSubscriber<T> extends Subscriber<T> {
    public final String TAG;

    public PragmaticSubscriber(String tag) {
        this.TAG = tag;
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: ", e);
    }
}
