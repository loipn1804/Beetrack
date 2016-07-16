package dne.beetrack.connection.callback;

import java.util.List;

import dne.beetrack.model.SessionHistory;

/**
 * Created by loipn on 7/16/2016.
 */
public interface UICallbackListHistory {
    void onSuccess(String message, List<SessionHistory> sessionHistoryList);
    void onFail(String error);
}
