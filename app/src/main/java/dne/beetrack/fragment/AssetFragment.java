package dne.beetrack.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.activity.MainActivity;
import dne.beetrack.adapter.AssetAdapter;
import dne.beetrack.adapter.SessionAdapter;
import dne.beetrack.adapter.SimpleStringAdapter;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.daocontroller.AssetController;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.view.ScrollInterfacedListView;
import greendao.Asset;
import greendao.Session;

/**
 * Created by USER on 06/16/2016.
 */
public class AssetFragment extends MyBaseFragment implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollInterfacedListView listView;
    private AssetAdapter adapter;

    private LinearLayout lnlSession;
    private TextView txtSessionName;

    private RelativeLayout rltSubmit;
    private TextView txtSubmit;

    private boolean isPull;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset, container, false);

        isPull = false;

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        listView = (ScrollInterfacedListView) view.findViewById(R.id.listView);
        lnlSession = (LinearLayout) view.findViewById(R.id.lnlSession);
        txtSessionName = (TextView) view.findViewById(R.id.txtSessionName);
        rltSubmit = (RelativeLayout) view.findViewById(R.id.rltSubmit);
        txtSubmit = (TextView) view.findViewById(R.id.txtSubmit);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.main_color);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPull = true;
                Session session = SessionController.getSessionChosen(getActivity());
                if (session != null) {
                    getListAssetBySession(session);
                } else {
                    showListSession();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        lnlSession.setOnClickListener(this);
        rltSubmit.setOnClickListener(this);

        listView.setOnDetectScrollListener(new ScrollInterfacedListView.OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
                if (rltSubmit.getVisibility() == View.INVISIBLE) {
                    rltSubmit.setVisibility(View.VISIBLE);
                    rltSubmit.setClickable(true);
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_add_btn_in);
                    rltSubmit.setAnimation(a);
                    a.start();
                }
            }

            @Override
            public void onDownScrolling() {
                if (rltSubmit.getVisibility() == View.VISIBLE) {
                    rltSubmit.setVisibility(View.INVISIBLE);
                    rltSubmit.setClickable(false);
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_add_btn_out);
                    rltSubmit.setAnimation(a);
                    a.start();
                }
            }
        });
    }

    private void initData() {
        List<Asset> listData = new ArrayList<>();

        adapter = new AssetAdapter(getActivity(), listData);
        listView.setAdapter(adapter);

        if (SessionController.getAll(getActivity()).size() == 0) {
            getListSession();
        } else {
            Session session = SessionController.getSessionChosen(getActivity());
            if (session != null) {
                txtSessionName.setText(session.getName());
                adapter.setListData(AssetController.getBySession(getActivity(), session.getSession_id()));
                int numScanned = AssetController.getNumberScannedOfSession(getActivity(), session.getSession_id());
                txtSubmit.setText(numScanned + "");
//                if (numScanned == 0) {
//                    rltSubmit.setVisibility(View.GONE);
//                } else {
//                    rltSubmit.setVisibility(View.VISIBLE);
//                }
            } else {
                rltSubmit.setVisibility(View.GONE);
                showListSession();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnlSession:
                showListSession();
                break;
            case R.id.rltSubmit:
                Session session = SessionController.getSessionChosen(getActivity());
                if (session != null) {
                    submitScanned(session);
                }
                break;
        }
    }

    private void showListSession() {
        final Dialog dialog = new Dialog(getActivity());

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        SessionAdapter.Callback callback = new SessionAdapter.Callback() {
            @Override
            public void onClickItem(Session session) {
                sessionChosen(session);
                dialog.dismiss();
            }
        };
        SessionAdapter adapter = new SessionAdapter(getActivity(), SessionController.getAll(getActivity()), callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private void sessionChosen(Session session) {
        Session sessionChosen = SessionController.getSessionChosen(getActivity());
        if (sessionChosen != null) {
            if (session.getSession_id() != sessionChosen.getSession_id()) {
                int numScanned = AssetController.getNumberScannedOfSession(getActivity(), sessionChosen.getSession_id());
                if (numScanned == 0) {
                    getListAssetBySession(session);
                } else {
                    submitScannedBeforeChangeSession(sessionChosen.getSession_id(), session);
                }
            }
        } else {
            getListAssetBySession(session);
        }
    }

    private void getListSession() {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                hideProgressDialog();
                showListSession();
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getListSession(getActivity(), callback, UserController.getCurrentUser(getActivity()).getAccount_id());
        showProgressDialog(false);
    }

    private void getListAssetBySession(final Session session) {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                if (!isDestroyView()) {
                    txtSessionName.setText(session.getName());
                    SessionController.setChosen(getActivity(), session);
                    adapter.setListData(AssetController.getBySession(getActivity(), session.getSession_id()));
                    int numScanned = AssetController.getNumberScannedOfSession(getActivity(), session.getSession_id());
                    txtSubmit.setText(numScanned + "");
//                    if (numScanned == 0) {
//                        rltSubmit.setVisibility(View.GONE);
//                    } else {
//                        rltSubmit.setVisibility(View.VISIBLE);
//                    }
                    if (isPull) {
                        isPull = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFail(String error) {
                if (!isDestroyView()) {
                    if (isPull) {
                        isPull = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getListAssetBySession(getActivity(), callback, session.getSession_id());
        if (!isPull) {
            showProgressDialog(false);
        }
    }

    private void submitScannedBeforeChangeSession(long session_id_submit, final Session sessionChange) {
        List<Asset> list = AssetController.getAssetScannedBySession(getActivity(), session_id_submit);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        String scan_date = format.format(date);

        JSONArray array = new JSONArray();
        for (Asset asset : list) {
            JSONObject object = new JSONObject();
            try {
                object.put("asset_id", asset.getAsset_id());
                object.put("session_id", asset.getSession_id());
                object.put("note", "");
                object.put("scan_date", scan_date);
                object.put("status", 1);

                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                getListAssetBySession(sessionChange);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.doScan(getActivity(), callback, UserController.getCurrentUser(getActivity()).getAccount_id(), array.toString());
        showProgressDialog(false);
    }

    private void submitScanned(final Session sessionSubmit) {
        List<Asset> list = AssetController.getAssetScannedBySession(getActivity(), sessionSubmit.getSession_id());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        String scan_date = format.format(date);

        JSONArray array = new JSONArray();
        for (Asset asset : list) {
            JSONObject object = new JSONObject();
            try {
                object.put("asset_id", asset.getAsset_id());
                object.put("session_id", asset.getSession_id());
                object.put("note", "");
                object.put("scan_date", scan_date);
                object.put("status", 1);

                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                getListAssetBySession(sessionSubmit);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.doScan(getActivity(), callback, UserController.getCurrentUser(getActivity()).getAccount_id(), array.toString());
        showProgressDialog(false);
    }
}
