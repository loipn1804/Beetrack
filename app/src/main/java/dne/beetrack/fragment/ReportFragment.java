package dne.beetrack.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dne.beetrack.R;

/**
 * Created by USER on 06/16/2016.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
