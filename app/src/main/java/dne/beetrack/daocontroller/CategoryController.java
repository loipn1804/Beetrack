package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.Category;
import greendao.CategoryDao;

/**
 * Created by loipn on 7/12/2016.
 */
public class CategoryController {

    private static CategoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCategoryDao();
    }

    public static void insertOrUpdate(Context context, Category mCategory) {
        if (getDao(context).load(mCategory.getCategory_id()) == null) {
            getDao(context).insert(mCategory);
        } else {
            getDao(context).update(mCategory);
        }
    }

    public static Category getByCategoryId(Context context, long category_id) {
        List<Category> list = getDao(context).queryRaw(" WHERE category_id = ?", category_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<Category> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
