package dne.beetrack.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dne.beetrack.R;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.daocontroller.AssetController;
import dne.beetrack.daocontroller.UserController;
import greendao.Asset;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by USER on 06/16/2016.
 */
public class ScanFragment extends MyBaseFragment implements View.OnClickListener, ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private FrameLayout frameLayout;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.content_frame);
    }

    private void initData() {
        handler = new Handler();

        mScannerView = new ZBarScannerView(getActivity());
        frameLayout.addView(mScannerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Asset asset = AssetController.getByBarcode(getActivity(), result.getContents());
        if (asset != null) {
            if (asset.getIs_scan() == 1) {
                showToastError(getString(R.string.asset_scan_already));
                resumeCamera();
            } else {
                asset.setIs_scan(1);
                AssetController.insertOrUpdate(getActivity(), asset);
                showToastOk(getString(R.string.scan_success));
                resumeCamera();
            }
        } else {
            showToastError(getString(R.string.barcode_not_found));
            resumeCamera();
        }
    }

    private void resumeCamera() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        }, 1000);
    }
}
