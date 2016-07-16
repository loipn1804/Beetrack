package dne.beetrack.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.activity.AssetDetailActivity;
import dne.beetrack.activity.SessionDetailActivity;
import dne.beetrack.model.SessionHistory;
import greendao.Session;

/**
 * Created by loipn on 7/16/2016.
 */
public class SessionReportAdapter extends BaseAdapter {

    private List<SessionHistory> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    public SessionReportAdapter(Activity activity, List<SessionHistory> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setListData(List<SessionHistory> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.item_session_report, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtCompleted = (TextView) convertView.findViewById(R.id.txtCompleted);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(listData.get(position).getName());
        holder.txtDate.setText(listData.get(position).getSession_date());
        if (listData.get(position).getF_completed() == 1) {
            holder.txtCompleted.setVisibility(View.VISIBLE);
        } else {
            holder.txtCompleted.setVisibility(View.INVISIBLE);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SessionDetailActivity.class);
                intent.putExtra("session_id", listData.get(position).getSession_id());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtName;
        TextView txtDate;
        TextView txtCompleted;
    }
}
