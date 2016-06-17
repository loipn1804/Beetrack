package dne.beetrack.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import dne.beetrack.R;
import dne.beetrack.activity.MyBaseActivity;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by USER on 06/16/2016.
 */
public class ScanFragment extends Fragment implements View.OnClickListener, ZBarScannerView.ResultHandler {

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
        ((MyBaseActivity) getActivity()).showToastInfo(result.getContents());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        }, 2000);
    }
}
