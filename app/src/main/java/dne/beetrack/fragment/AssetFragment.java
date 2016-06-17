package dne.beetrack.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.adapter.AssetAdapter;

/**
 * Created by USER on 06/16/2016.
 */
public class AssetFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private AssetAdapter adapter;
    private List<String> listData;

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
    }

    private void initData() {
        listData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listData.add("");
        }

        adapter = new AssetAdapter(getActivity(), listData);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
