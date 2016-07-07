package dne.beetrack.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.activity.MainActivity;
import dne.beetrack.adapter.AssetAdapter;
import dne.beetrack.adapter.SimpleStringAdapter;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.UserController;
import greendao.Session;

/**
 * Created by USER on 06/16/2016.
 */
public class AssetFragment extends MyBaseFragment implements View.OnClickListener {

    private ListView listView;
    private AssetAdapter adapter;
    private List<String> listData;

    private LinearLayout lnlSession;
    private TextView txtSessionName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
        lnlSession = (LinearLayout) view.findViewById(R.id.lnlSession);
        txtSessionName = (TextView) view.findViewById(R.id.txtSessionName);

        lnlSession.setOnClickListener(this);
    }

    private void initData() {
        listData = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            listData.add("");
//        }

        adapter = new AssetAdapter(getActivity(), listData);
        listView.setAdapter(adapter);

        getListSession();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnlSession:
                showListSession();
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
        List<String> list = new ArrayList<>();
        final List<Session> sessions = SessionController.getAll(getActivity());
        for (Session session : sessions) {
            list.add(session.getName());
        }
        SimpleStringAdapter adapter = new SimpleStringAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sessionChosen(sessions.get(position));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sessionChosen(Session session) {
        txtSessionName.setText(session.getName());
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
}
