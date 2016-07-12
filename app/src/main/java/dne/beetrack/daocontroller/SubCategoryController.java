package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.Category;
import greendao.SubCategory;
import greendao.SubCategoryDao;

/**
 * Created by loipn on 7/12/2016.
 */
public class SubCategoryController {

    private static SubCategoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getSubCategoryDao();
    }

    public static void insertOrUpdate(Context context, SubCategory mSubCategory) {
        if (getDao(context).load(mSubCategory.getSub_category_id()) == null) {
            getDao(context).insert(mSubCategory);
        } else {
            getDao(context).update(mSubCategory);
        }
    }

    public static SubCategory getBySubCategoryId(Context context, long sub_category_id) {
        List<SubCategory> list = getDao(context).queryRaw(" WHERE sub_category_id = ?", sub_category_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<SubCategory> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static List<SubCategory> getAllByCategoryId(Context context, long category_id) {
        if (category_id == 0) {
            return getAll(context);
        } else {
            return getDao(context).queryRaw(" WHERE category_id = ?", category_id + "");
        }
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
