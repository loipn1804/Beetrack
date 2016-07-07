package dne.beetrack.connection.callback;

/**
 * Created by USER on 3/11/2016.
 */
public interface ApiCallback {
    void onSuccess(String result);
    void onFail(String error);
}
