package dne.beetrack.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.activity.FilterActivity;
import dne.beetrack.adapter.AssetAdapter;
import dne.beetrack.adapter.SessionAdapter;
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

    private int REQUEST_FILTER = 123;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollInterfacedListView listView;
    private AssetAdapter adapter;

    private LinearLayout lnlSession;
    private TextView txtSessionName;

    private FloatingActionButton btnSubmit;
    private FloatingActionButton btnFilter;
    private FloatingActionButton btnChangeSession;

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
        btnSubmit = (FloatingActionButton) view.findViewById(R.id.btnSubmit);
        btnFilter = (FloatingActionButton) view.findViewById(R.id.btnFilter);
        btnChangeSession = (FloatingActionButton) view.findViewById(R.id.btnChangeSession);
        
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

        swipeRefreshLayout.setRefreshing(true);

        btnSubmit.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnChangeSession.setOnClickListener(this);
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
//                adapter.setListData(AssetController.getBySession(getActivity(), session.getSession_id()));
                adapter.setListData(AssetController.getBySessionFiltered(getActivity(), session.getSession_id()));
            } else {
                showListSession();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                Session session = SessionController.getSessionChosen(getActivity());
                if (session != null) {
                    submitScanned(session);
                }
                break;
            case R.id.btnFilter:
                Intent intentFilter = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intentFilter, REQUEST_FILTER);
                break;
            case R.id.btnChangeSession:
                showListSession();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILTER && resultCode == getActivity().RESULT_OK) {
            Session sessionChosen = SessionController.getSessionChosen(getActivity());
            if (sessionChosen != null) {
                adapter.setListData(AssetController.getBySessionFiltered(getActivity(), sessionChosen.getSession_id()));
            }
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

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(getString(R.string.choose_session));
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
//                    adapter.setListData(AssetController.getBySession(getActivity(), session.getSession_id()));
                    adapter.setListData(AssetController.getBySessionFiltered(getActivity(), session.getSession_id()));
                    if (isPull) {
                        isPull = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    SharedPreferences preferences = getActivity().getSharedPreferences("filter", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
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
