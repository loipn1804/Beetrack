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

    public static void setChosen(Context context, Session mSession) {
        List<Session> list = getDao(context).loadAll();
        for (Session session : list) {
            session.setIs_chosen(0);
            getDao(context).update(session);
        }
        mSession.setIs_chosen(1);
        getDao(context).update(mSession);
    }

    public static Session getSessionChosen(Context context) {
        List<Session> list = getDao(context).queryRaw(" WHERE is_chosen = 1");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void delete(Context context, Session mSession) {
        getDao(context).delete(mSession);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
