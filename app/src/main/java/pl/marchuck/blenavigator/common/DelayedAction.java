package pl.marchuck.blenavigator.common;

import rx.functions.Action1;

/**
 * @author Lukasz Marczak
 * @since 10.07.16.
 */
public abstract class DelayedAction implements Action1<Long> {
    private final int time;

    public int getTime() {
        return time;
    }

    public DelayedAction(int time) {
        this.time = time;
    }
}
