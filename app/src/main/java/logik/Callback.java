package logik;

/**
 * Created by KimdR on 19-11-2017.
 */

public interface Callback {
    void onEventCompleted(String msg);
    void onEventFailed(String msg);

}
