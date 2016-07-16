package dne.beetrack.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.adapter.SessionReportAdapter;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.connection.callback.UICallbackListHistory;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.model.SessionHistory;
import greendao.Session;

/**
 * Created by USER on 06/16/2016.
 */
public class ReportFragment extends MyBaseFragment implements View.OnClickListener {

    private ListView listView;
    private SessionReportAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
    }

    private void initData() {
        getListHistory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void getListHistory() {
        UICallbackListHistory callback = new UICallbackListHistory() {
            @Override
            public void onSuccess(String message, List<SessionHistory> sessionHistoryList) {
                showToastOk(message);
                hideProgressDialog();
                adapter = new SessionReportAdapter(getActivity(), sessionHistoryList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getListHistory(getActivity(), callback, UserController.getCurrentUser(getActivity()).getAccount_id());
        showProgressDialog(false);
    }
}