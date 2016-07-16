package dne.beetrack.connection;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.activity.MainActivity;
import dne.beetrack.connection.callback.ApiCallback;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.connection.callback.UICallbackHistoryDetail;
import dne.beetrack.connection.callback.UICallbackListHistory;
import dne.beetrack.connection.thread.BackgroundThreadExecutor;
import dne.beetrack.connection.thread.UIThreadExecutor;
import dne.beetrack.daocontroller.AssetController;
import dne.beetrack.daocontroller.CategoryController;
import dne.beetrack.daocontroller.DepartmentController;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.SubCategoryController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.daocontroller.WarehouseController;
import dne.beetrack.model.LostAsset;
import dne.beetrack.model.SessionHistory;
import dne.beetrack.model.SessionUser;
import dne.beetrack.staticfunction.StaticFunction;
import greendao.Asset;
import greendao.Category;
import greendao.Department;
import greendao.Session;
import greendao.SubCategory;
import greendao.User;
import greendao.Warehouse;

/**
 * Created by loipn on 7/5/2016.
 */
public class Executor {

    public static void login(final Context context, final UICallback callback, final String username, final String password) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                long account_id = data.getLong("account_id");
                                long company_id = data.getLong("company_id");
                                long department_id = data.getLong("department_id");
                                String name = data.getString("name");
                                String email = data.getString("email");
                                String username = data.getString("username");
                                String phone = data.getString("phone");
                                String created_at = data.getString("created_at");
                                String updated_at = data.getString("updated_at");

                                User user = new User(account_id, company_id, department_id, name, email, username, phone, created_at, updated_at);
                                UserController.insertOrUpdate(context, user);

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.login(apiCallback, username, password);
            }
        });
    }

    public static void getListSession(final Context context, final UICallback callback, final long account_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                SessionController.clearAll(context);
                                JSONArray dataArr = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject data = dataArr.getJSONObject(i);
                                    long session_id = data.getLong("session_id");
                                    long company_id = data.getLong("company_id");
                                    String name = data.getString("name");
                                    String description = data.getString("description");
                                    String session_date = data.getString("session_date");
                                    String created_at = data.getString("created_at");
                                    String updated_at = data.getString("updated_at");
                                    int f_completed = data.getInt("f_completed");

                                    Session session = new Session(session_id, company_id, name, description, session_date, created_at, updated_at, 0, f_completed);
                                    SessionController.insertOrUpdate(context, session);
                                }

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getListSession(apiCallback, account_id);
            }
        });
    }

    public static void getListHistory(final Context context, final UICallbackListHistory callback, final long account_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                JSONArray dataArr = jsonObject.getJSONArray("data");
                                final List<SessionHistory> sessionHistoryList = new ArrayList<>();
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject data = dataArr.getJSONObject(i);
                                    long session_id = data.getLong("session_id");
                                    long company_id = data.getLong("company_id");
                                    String name = data.getString("name");
                                    String description = data.getString("description");
                                    String session_date = data.getString("session_date");
                                    String created_at = data.getString("created_at");
                                    String updated_at = data.getString("updated_at");
                                    int f_completed = data.getInt("f_completed");

                                    SessionHistory sessionHistory = new SessionHistory(session_id, company_id, name, description, session_date, f_completed, created_at, updated_at, 0, 0, 0, null, null);
                                    sessionHistoryList.add(sessionHistory);
                                }

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message, sessionHistoryList);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getListHistory(apiCallback, account_id);
            }
        });
    }

    public static void getSessionDetail(final Context context, final UICallbackHistoryDetail callback, final long session_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                long session_id = data.getLong("session_id");
                                long company_id = data.getLong("company_id");
                                String name = data.getString("name");
                                String description = data.getString("description");
                                String session_date = data.getString("session_date");
                                int f_completed = data.getInt("f_completed");
                                String created_at = data.getString("created_at");
                                String updated_at = data.getString("updated_at");
                                int total_scanned = data.getInt("total_scanned");
                                int total_assets = data.getInt("total_assets");

                                JSONArray arrayUser = data.getJSONArray("users");
                                List<SessionUser> sessionUserList = new ArrayList<>();
                                for (int j = 0; j < arrayUser.length(); j++) {
                                    JSONObject objUser = arrayUser.getJSONObject(j);
                                    long account_id = objUser.getLong("account_id");
                                    long department_id = objUser.getLong("department_id");
                                    String name_user = objUser.getString("name");
                                    String username = objUser.getString("username");
                                    String email = objUser.getString("email");
                                    String phone = objUser.getString("phone");
                                    int f_completed_user = objUser.getInt("f_completed");

                                    SessionUser sessionUser = new SessionUser(account_id, company_id, department_id, name_user, username, email, phone, f_completed_user);
                                    sessionUserList.add(sessionUser);
                                }

                                int total_lost = 0;
                                List<LostAsset> lostAssetList = new ArrayList<>();
                                if (f_completed == 1) {
                                    total_lost = data.getInt("total_lost");
                                    JSONArray arrayAsset = data.getJSONArray("assets_lost");
                                    for (int k = 0; k < arrayAsset.length(); k++) {
                                        JSONObject asset = arrayAsset.getJSONObject(k);
                                        String name_asset = asset.getString("name");
                                        String asset_code = asset.getString("asset_code");

                                        lostAssetList.add(new LostAsset(name_asset, asset_code));
                                    }
                                }

                                final SessionHistory sessionHistory = new SessionHistory(session_id, company_id, name, description, session_date, f_completed, created_at, updated_at, total_scanned, total_assets, total_lost, sessionUserList, lostAssetList);

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message, sessionHistory);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getSessionDetail(apiCallback, session_id);
            }
        });
    }

    public static void confirmSession(final Context context, final UICallback callback, final long session_id, final long account_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.confirmSession(apiCallback, session_id, account_id);
            }
        });
    }

    public static void getListAssetBySession(final Context context, final UICallback callback, final long session_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                AssetController.clearBySession(context, session_id);
                                JSONArray dataArr = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject data = dataArr.getJSONObject(i);
                                    long asset_id = data.getLong("asset_id");
                                    String asset_code = data.getString("asset_code");
                                    long company_id = data.getLong("company_id");
                                    long department_id = data.getLong("department_id");
                                    long category_id = data.getLong("category_id");
                                    long sub_category_id = data.getLong("sub_category_id");
                                    long warehouse_id = 0;
                                    try {
                                        warehouse_id = data.getLong("warehouse_id");
                                    } catch (JSONException wareEx) {

                                    }
                                    String seri = data.getString("seri");
                                    seri = seri.equals("null") ? "" : seri;
                                    String warehouse_seri = data.getString("warehouse_seri");
                                    warehouse_seri = warehouse_seri.equals("null") ? "" : warehouse_seri;
                                    String user_using = data.getString("user_using");
                                    user_using = user_using.equals("null") ? "" : user_using;
                                    String name = data.getString("name");
                                    int f_active = data.getInt("f_active");
                                    String created_at = data.getString("created_at");
                                    String updated_at = data.getString("updated_at");
                                    String department_name = data.getString("department_name");
                                    department_name = department_name.equals("null") ? "" : department_name;
                                    long session_id = data.getLong("session_id");
                                    int status = data.getInt("status");
                                    int is_scan = 0;
                                    String category_name = data.getString("category_name");
                                    category_name = category_name.equals("null") ? "" : category_name;
                                    String sub_category_name = data.getString("sub_category_name");
                                    sub_category_name = sub_category_name.equals("null") ? "" : sub_category_name;
                                    String warehouse_name = data.getString("warehouse_name");
                                    warehouse_name = warehouse_name.equals("null") ? "" : warehouse_name;

                                    Asset asset = new Asset(asset_id, asset_code, company_id, department_id, category_id, sub_category_id, warehouse_id, seri, warehouse_seri, user_using, name, f_active, created_at, updated_at, department_name, session_id, status, is_scan, category_name, sub_category_name, warehouse_name);
                                    AssetController.insertOrUpdate(context, asset);
                                }

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getListAssetBySession(apiCallback, session_id);
            }
        });
    }

    public static void doScan(final Context context, final UICallback callback, final long account_id, final String list) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.doScan(apiCallback, account_id, list);
            }
        });
    }

    public static void getListCategory(final Context context, final UICallback callback, final long company_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                JSONArray dataArr = jsonObject.getJSONArray("data");
                                CategoryController.clearAll(context);
                                SubCategoryController.clearAll(context);
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject data = dataArr.getJSONObject(i);
                                    long category_id = data.getLong("category_id");
                                    String category_name = data.getString("name");

                                    Category category = new Category(category_id, category_name);
                                    CategoryController.insertOrUpdate(context, category);

                                    JSONArray subCategoryArr = data.getJSONArray("sub_categories");
                                    for (int j = 0; j < subCategoryArr.length(); j++) {
                                        JSONObject subCategoryObj = subCategoryArr.getJSONObject(j);
                                        long sub_category_id = subCategoryObj.getLong("sub_category_id");
                                        String sub_category_name = subCategoryObj.getString("name");

                                        SubCategory subCategory = new SubCategory(sub_category_id, category_id, sub_category_name);
                                        SubCategoryController.insertOrUpdate(context, subCategory);
                                    }
                                }

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getListCategory(apiCallback, company_id);
            }
        });
    }

    public static void getListDepartment(final Context context, final UICallback callback, final long company_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                JSONArray dataArr = jsonObject.getJSONArray("data");
                                DepartmentController.clearAll(context);
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject data = dataArr.getJSONObject(i);
                                    long department_id = data.getLong("department_id");
                                    String department_code = data.getString("department_code");
                                    String department_name = data.getString("name");
                                    String description = data.getString("description");

                                    Department department = new Department(department_id, department_code, department_name, description);
                                    DepartmentController.insertOrUpdate(context, department);
                                }

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getListDepartment(apiCallback, company_id);
            }
        });
    }

    public static void getListWarehouse(final Context context, final UICallback callback, final long company_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                JSONArray dataArr = jsonObject.getJSONArray("data");
                                WarehouseController.clearAll(context);
                                for (int i = 0; i < dataArr.length(); i++) {
                                    JSONObject data = dataArr.getJSONObject(i);
                                    long warehouse_id = data.getLong("warehouse_id");
                                    String warehouse_code = data.getString("warehouse_code");
                                    String warehouse_name = data.getString("name");

                                    Warehouse warehouse = new Warehouse(warehouse_id, warehouse_code, warehouse_name);
                                    WarehouseController.insertOrUpdate(context, warehouse);
                                }

                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.getListWarehouse(apiCallback, company_id);
            }
        });
    }

    public static void editAsset(final Context context, final UICallback callback, final long asset_id, final String field_name, final String data) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onFail(context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onSuccess(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean success = jsonObject.getBoolean("success");
                            final String message = jsonObject.getString("message");
                            if (success) {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(message);
                                    }
                                });
                            } else {
                                UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(message);
                                    }
                                });
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(final String error) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(error);
                            }
                        });
                    }
                };
                Api api = new Api();
                api.editAsset(apiCallback, asset_id, field_name, data);
            }
        });
    }
}
