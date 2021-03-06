package dne.beetrack.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dne.beetrack.R;
import dne.beetrack.daocontroller.AssetController;
import dne.beetrack.daocontroller.CategoryController;
import dne.beetrack.daocontroller.DepartmentController;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.SubCategoryController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.daocontroller.WarehouseController;
import dne.beetrack.fragment.AssetFragment;
import dne.beetrack.fragment.ReportFragment;
import dne.beetrack.fragment.ScanFragment;

public class MainActivity extends MyBaseActivity implements View.OnClickListener {

    private LinearLayout lnlAsset;
    private LinearLayout lnlScan;
    private LinearLayout lnlReport;
    private LinearLayout lnlExit;

    private ImageView imvAsset;
    private ImageView imvScan;
    private ImageView imvReport;
    private ImageView imvExit;

    private TextView txtAsset;
    private TextView txtScan;
    private TextView txtReport;
    private TextView txtExit;

    private AssetFragment assetFragment;
    private ScanFragment scanFragment;
    private ReportFragment reportFragment;

    private int ASSET = 1;
    private int SCAN = 2;
    private int REPORT = 3;
    private int CURRENT_MODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        lnlAsset = (LinearLayout) findViewById(R.id.lnlAsset);
        lnlScan = (LinearLayout) findViewById(R.id.lnlScan);
        lnlReport = (LinearLayout) findViewById(R.id.lnlReport);
        lnlExit = (LinearLayout) findViewById(R.id.lnlExit);

        imvAsset = (ImageView) findViewById(R.id.imvAsset);
        imvScan = (ImageView) findViewById(R.id.imvScan);
        imvReport = (ImageView) findViewById(R.id.imvReport);
        imvExit = (ImageView) findViewById(R.id.imvExit);

        txtAsset = (TextView) findViewById(R.id.txtAsset);
        txtScan = (TextView) findViewById(R.id.txtScan);
        txtReport = (TextView) findViewById(R.id.txtReport);
        txtExit = (TextView) findViewById(R.id.txtExit);

        lnlAsset.setOnClickListener(this);
        lnlScan.setOnClickListener(this);
        lnlReport.setOnClickListener(this);
        lnlExit.setOnClickListener(this);
    }

    private void initData() {
        switchFragment(ASSET);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnlAsset:
                if (CURRENT_MODE != ASSET)
                    switchFragment(ASSET);
                else showToastInfo("already");
                break;
            case R.id.lnlScan:
                if (CURRENT_MODE != SCAN)
                    switchFragment(SCAN);
                else showToastInfo("already");
                break;
            case R.id.lnlReport:
                if (CURRENT_MODE != REPORT) {
                    switchFragment(REPORT);
                }
                break;
            case R.id.lnlExit:
                showPopupConfirmLogout(getString(R.string.confirm_exit));
                break;
        }
    }

    public void showPopupConfirmLogout(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtMessage.setText(message);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void logout() {
        UserController.clearAll(this);
        SessionController.clearAll(this);
        AssetController.clearAll(this);
        CategoryController.clearAll(this);
        SubCategoryController.clearAll(this);
        DepartmentController.clearAll(this);
        WarehouseController.clearAll(this);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchFragment(int fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment == ASSET) {
            if (assetFragment == null)
                assetFragment = new AssetFragment();
            fragmentTransaction.replace(R.id.lnlContent, assetFragment);
            fragmentTransaction.commit();
        } else if (fragment == SCAN) {
            if (scanFragment == null)
                scanFragment = new ScanFragment();
            fragmentTransaction.replace(R.id.lnlContent, scanFragment);
            fragmentTransaction.commit();
        } else if (fragment == REPORT) {
            if (reportFragment == null)
                reportFragment = new ReportFragment();
            fragmentTransaction.replace(R.id.lnlContent, reportFragment);
            fragmentTransaction.commit();
        }

        CURRENT_MODE = fragment;
        setLayoutFooter();
    }

    private void setLayoutFooter() {
        if (CURRENT_MODE == ASSET) {
            imvAsset.setImageResource(R.drawable.ic_asset_main);
            txtAsset.setTextColor(getResources().getColor(R.color.main_color));
            imvScan.setImageResource(R.drawable.ic_scan_gray);
            txtScan.setTextColor(getResources().getColor(R.color.txt_black_99));
            imvReport.setImageResource(R.drawable.ic_report_gray);
            txtReport.setTextColor(getResources().getColor(R.color.txt_black_99));
        } else if (CURRENT_MODE == SCAN) {
            imvAsset.setImageResource(R.drawable.ic_asset_gray);
            txtAsset.setTextColor(getResources().getColor(R.color.txt_black_99));
            imvScan.setImageResource(R.drawable.ic_scan_main);
            txtScan.setTextColor(getResources().getColor(R.color.main_color));
            imvReport.setImageResource(R.drawable.ic_report_gray);
            txtReport.setTextColor(getResources().getColor(R.color.txt_black_99));
        } else if (CURRENT_MODE == REPORT) {
            imvAsset.setImageResource(R.drawable.ic_asset_gray);
            txtAsset.setTextColor(getResources().getColor(R.color.txt_black_99));
            imvScan.setImageResource(R.drawable.ic_scan_gray);
            txtScan.setTextColor(getResources().getColor(R.color.txt_black_99));
            imvReport.setImageResource(R.drawable.ic_report_main);
            txtReport.setTextColor(getResources().getColor(R.color.main_color));
        }
    }

    private void fakeData() {

    }
}