package dne.beetrack.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;

/**
 * Created by loipn on 7/12/2016.
 */
public class OptionAdapter extends BaseAdapter {

    public interface Callback {
        void onClickItem(int position);
    }

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Callback callback;

    public OptionAdapter(Activity activity, List<String> listData, Callback callback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.callback = callback;
    }

    public void setListData(List<String> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_option, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(listData.get(position));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItem(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtName;
    }
}
