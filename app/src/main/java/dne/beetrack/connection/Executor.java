package dne.beetrack.connection;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dne.beetrack.R;
import dne.beetrack.activity.MainActivity;
import dne.beetrack.connection.callback.ApiCallback;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.connection.thread.BackgroundThreadExecutor;
import dne.beetrack.connection.thread.UIThreadExecutor;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.staticfunction.StaticFunction;
import greendao.Session;
import greendao.User;

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
                                    long account_id = data.getLong("account_id");
                                    long company_id = data.getLong("company_id");
                                    String name = data.getString("name");
                                    String description = data.getString("description");
                                    String session_date = data.getString("session_date");
                                    String created_at = data.getString("created_at");
                                    String updated_at = data.getString("updated_at");

                                    Session session = new Session(session_id, account_id, company_id, name, description, session_date, created_at, updated_at, false);
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
}
