package dne.beetrack.daocontroller;

import android.content.Context;

import java.util.List;

import dne.beetrack.application.MyApplication;
import greendao.Asset;
import greendao.AssetDao;
import greendao.Session;

/**
 * Created by loipn on 7/7/2016.
 */
public class AssetController {

    private static AssetDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getAssetDao();
    }

    public static void insertOrUpdate(Context context, Asset mAsset) {
        if (getDao(context).load(mAsset.getAsset_id()) == null) {
            getDao(context).insert(mAsset);
        } else {
            getDao(context).update(mAsset);
        }
    }

    public static List<Asset> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static List<Asset> getBySession(Context context, long session_id) {
        return getDao(context).queryRaw(" WHERE session_id = ?", session_id + "");
    }

    public static Asset getByBarcode(Context context, String barcode) {
        Session session = SessionController.getSessionChosen(context);
        if (session != null) {
            List<Asset> list = getDao(context).queryRaw(" WHERE session_id = ? AND asset_code = ?", session.getSession_id() + "", barcode);
            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Asset getById(Context context, long asset_id) {
        return getDao(context).loadByRowId(asset_id);
    }

    public static void delete(Context context, Asset mAsset) {
        getDao(context).delete(mAsset);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }

    public static void clearBySession(Context context, long session_id) {
        List<Asset> list = getDao(context).queryRaw(" WHERE session_id = ?", session_id + "");
        for (Asset asset : list) {
            getDao(context).delete(asset);
        }
    }
}
