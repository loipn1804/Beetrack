package dne.beetrack.daocontroller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
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

    public static List<Asset> getBySessionFiltered(Context context, long session_id) {
        SharedPreferences preferences = context.getSharedPreferences("filter", context.MODE_PRIVATE);
        boolean scanned = preferences.getBoolean("scanned", true);
        boolean not_scan_yet = preferences.getBoolean("not_scan_yet", true);
        long category_id = preferences.getLong("category_id", 0);
        long sub_category_id = preferences.getLong("sub_category_id", 0);
        long department_id = preferences.getLong("department_id", 0);
        List<Asset> list = getDao(context).queryRaw(" WHERE session_id = ?", session_id + "");
        List<Asset> listFiltered = new ArrayList<>();
        for (Asset asset : list) {
            if (!scanned) {
                if (asset.getIs_scan() == 1 || asset.getStatus() == 1) {
                    continue;
                }
            }
            if (!not_scan_yet) {
                if (asset.getIs_scan() == 0 && asset.getStatus() == 0) {
                    continue;
                }
            }
            if (category_id > 0) {
                if (asset.getCategory_id() != category_id) {
                    continue;
                }
            }
            if (sub_category_id > 0) {
                if (asset.getSub_category_id() != sub_category_id) {
                    continue;
                }
            }
            if (department_id > 0) {
                if (asset.getDepartment_id() != department_id) {
                    continue;
                }
            }
            listFiltered.add(asset);
        }
        return listFiltered;
    }

    public static List<Asset> getAssetScannedBySession(Context context, long session_id) {
        return getDao(context).queryRaw(" WHERE is_scan = ? AND session_id = ?", "1", session_id + "");
    }

    public static int getNumberScannedOfSession(Context context, long session_id) {
        return getDao(context).queryRaw(" WHERE is_scan = ? AND session_id = ?", "1", session_id + "").size();
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
