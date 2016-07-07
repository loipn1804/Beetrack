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
            if (asset.getStatus() == 1) {
                showToastError(getString(R.string.asset_scan_already));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScannerView.resumeCameraPreview(ScanFragment.this);
                    }
                }, 2000);
            } else {
                doScan(asset);
            }
        } else {
            showToastError(getString(R.string.barcode_not_found));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ScanFragment.this);
                }
            }, 2000);
        }
    }

    private void doScan(final Asset asset) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        String scan_date = format.format(date);

        JSONArray array = new JSONArray();
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

        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                hideProgressDialog();
                asset.setStatus(1);
                AssetController.insertOrUpdate(getActivity(), asset);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScannerView.resumeCameraPreview(ScanFragment.this);
                    }
                }, 2000);
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
