package dne.beetrack.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.adapter.SimpleStringAdapter;

/**
 * Created by USER on 06/16/2016.
 */
public class ReportFragment extends MyBaseFragment implements View.OnClickListener {

    private ListView listView;

    private FloatingActionsMenu floatingActionsMenu;

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

        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.floatingActionsMenu);
        floatingActionsMenu.expand();
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("abcdef");
        }

        SimpleStringAdapter adapter = new SimpleStringAdapter(getActivity(), list);
        listView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
