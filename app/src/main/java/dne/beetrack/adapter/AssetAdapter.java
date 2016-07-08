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
import greendao.Asset;

/**
 * Created by user on 3/21/2016.
 */
public class AssetAdapter extends BaseAdapter {

    private List<Asset> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    public AssetAdapter(Activity activity, List<Asset> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setListData(List<Asset> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_asset, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtAssetName = (TextView) convertView.findViewById(R.id.txtAssetName);
            holder.txtAssetCode = (TextView) convertView.findViewById(R.id.txtAssetCode);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtScanned = (TextView) convertView.findViewById(R.id.txtScanned);
            holder.imvStatus = (ImageView) convertView.findViewById(R.id.imvStatus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtAssetName.setText(listData.get(position).getName());
        holder.txtAssetCode.setText(listData.get(position).getAsset_code());
        holder.txtTime.setText(listData.get(position).getCreated_at());
        if (listData.get(position).getStatus() == 1) {
            holder.imvStatus.setVisibility(View.VISIBLE);
            holder.txtScanned.setVisibility(View.GONE);
        } else {
            holder.imvStatus.setVisibility(View.INVISIBLE);
            if (listData.get(position).getIs_scan() == 1) {
                holder.txtScanned.setVisibility(View.VISIBLE);
            } else {
                holder.txtScanned.setVisibility(View.GONE);
            }
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AssetDetailActivity.class);
                intent.putExtra("asset_id", listData.get(position).getAsset_id());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtAssetName;
        TextView txtAssetCode;
        TextView txtTime;
        TextView txtScanned;
        ImageView imvStatus;
    }
}
