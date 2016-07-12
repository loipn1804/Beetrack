package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.Department;
import greendao.DepartmentDao;

/**
 * Created by loipn on 7/12/2016.
 */
public class DepartmentController {

    private static DepartmentDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDepartmentDao();
    }

    public static void insertOrUpdate(Context context, Department mDepartment) {
        if (getDao(context).load(mDepartment.getDepartment_id()) == null) {
            getDao(context).insert(mDepartment);
        } else {
            getDao(context).update(mDepartment);
        }
    }

    public static Department getByDepartmentId(Context context, long department_id) {
        List<Department> list = getDao(context).queryRaw(" WHERE department_id = ?", department_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<Department> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
