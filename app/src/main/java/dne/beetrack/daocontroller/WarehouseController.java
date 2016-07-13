package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.Warehouse;
import greendao.WarehouseDao;

/**
 * Created by loipn on 7/13/2016.
 */
public class WarehouseController {

    private static WarehouseDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getWarehouseDao();
    }

    public static void insertOrUpdate(Context context, Warehouse mWarehouse) {
        if (getDao(context).load(mWarehouse.getWarehouse_id()) == null) {
            getDao(context).insert(mWarehouse);
        } else {
            getDao(context).update(mWarehouse);
        }
    }

    public static Warehouse getByWarehouseId(Context context, long warehouse_id) {
        List<Warehouse> list = getDao(context).queryRaw(" WHERE warehouse_id = ?", warehouse_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<Warehouse> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
