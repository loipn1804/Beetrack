package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.Session;
import greendao.SessionDao;

/**
 * Created by loipn on 7/7/2016.
 */
public class SessionController {

    private static SessionDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getSessionDao();
    }

    public static void insertOrUpdate(Context context, Session mSession) {
        if (getDao(context).load(mSession.getSession_id()) == null) {
            getDao(context).insert(mSession);
        } else {
            getDao(context).update(mSession);
        }
    }

    public static List<Session> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void delete(Context context, Session mSession) {
        getDao(context).delete(mSession);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
