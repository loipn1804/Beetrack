package dne.beetrack.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import dne.beetrack.R;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallbackHistoryDetail;
import dne.beetrack.model.LostAsset;
import dne.beetrack.model.SessionHistory;
import dne.beetrack.model.SessionUser;
import greendao.Asset;

/**
 * Created by loipn on 7/16/2016.
 */
public class SessionDetailActivity extends MyBaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;

    private TextView txtSessionName;
    private TextView txtSessionDate;
    private TextView txtTotal;
    private TextView txtScanned;
    private TextView txtLost;
    private TextView txtUsers;
    private LinearLayout lnlUsers;
    private TextView txtLostAssets;
    private LinearLayout lnlLostAssets;

    private long session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);

        if (getIntent().hasExtra("session_id")) {
            session_id = getIntent().getLongExtra("session_id", 0);
            if (session_id > 0) {
                initView();
                initData();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtSessionName = (TextView) findViewById(R.id.txtSessionName);
        txtSessionDate = (TextView) findViewById(R.id.txtSessionDate);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtScanned = (TextView) findViewById(R.id.txtScanned);
        txtLost = (TextView) findViewById(R.id.txtLost);
        txtUsers = (TextView) findViewById(R.id.txtUsers);
        lnlUsers = (LinearLayout) findViewById(R.id.lnlUsers);
        txtLostAssets = (TextView) findViewById(R.id.txtLostAssets);
        lnlLostAssets = (LinearLayout) findViewById(R.id.lnlLostAssets);

        rltBack.setOnClickListener(this);
    }

    private void initData() {
        getSessionDetail();
    }

    private void setData(SessionHistory sessionHistory) {
        txtSessionName.setText(sessionHistory.getName());
        txtSessionDate.setText(getString(R.string.date) + ": " + sessionHistory.getSession_date());
        txtTotal.setText(getString(R.string.total_asset) + ": " + sessionHistory.getTotal_assets() + "");
        txtScanned.setText(getString(R.string.scanned) + ": " + sessionHistory.getTotal_scanned() + "");
        setListUser(sessionHistory.getSessionUserList());

        if (sessionHistory.getF_completed() == 1) {
            txtLost.setText(getString(R.string.lost_asset) + ": " + sessionHistory.getTotal_lost() + "");
            txtLost.setVisibility(View.VISIBLE);
            txtLostAssets.setVisibility(View.VISIBLE);
            setListLostAsset(sessionHistory.getLostAssetList());
        } else {
            txtLost.setVisibility(View.GONE);
            txtLostAssets.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
        }
    }

    private void setListUser(List<SessionUser> users) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lnlUsers.removeAllViews();

        for (SessionUser user : users) {
            View view = inflater.inflate(R.layout.item_user_of_session, null);
            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            txtName.setText(user.getName());

            lnlUsers.addView(view);
        }
    }

    private void setListLostAsset(List<LostAsset> assets) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lnlLostAssets.removeAllViews();

        for (LostAsset asset : assets) {
            View view = inflater.inflate(R.layout.item_lost_asset, null);
            TextView txtAssetName = (TextView) view.findViewById(R.id.txtAssetName);
            TextView txtAssetCode = (TextView) view.findViewById(R.id.txtAssetCode);
            txtAssetName.setText(asset.getName());
            txtAssetCode.setText(asset.getAsset_code());

            lnlLostAssets.addView(view);
        }
    }

    private void getSessionDetail() {
        UICallbackHistoryDetail callback = new UICallbackHistoryDetail() {
            @Override
            public void onSuccess(String message, SessionHistory sessionHistory) {
//                showToastOk(message);
                hideProgressDialog();
                setData(sessionHistory);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getSessionDetail(this, callback, session_id);
        showProgressDialog(false);
    }
}
