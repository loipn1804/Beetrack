package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.User;
import greendao.UserDao;

/**
 * Created by USER on 2/16/2016.
 */
public class UserController {

    private static UserDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getUserDao();
    }

    public static void insertOrUpdate(Context context, User mUser) {
        if (getDao(context).load(mUser.getAccount_id()) == null) {
            getDao(context).insert(mUser);
        } else {
            getDao(context).update(mUser);
        }
    }

    public static boolean isLogin(Context context) {
        List<User> list = getDao(context).loadAll();
        if (list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static User getCurrentUser(Context context) {
        List<User> list = getDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void delete(Context context, User mUser) {
        getDao(context).delete(mUser);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
