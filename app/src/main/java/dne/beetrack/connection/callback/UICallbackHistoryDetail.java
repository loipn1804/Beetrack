package dne.beetrack.connection.callback;

import java.util.List;

import dne.beetrack.model.SessionHistory;

/**
 * Created by loipn on 7/16/2016.
 */
public interface UICallbackHistoryDetail {
    void onSuccess(String message, SessionHistory sessionHistory);
    void onFail(String error);
}
