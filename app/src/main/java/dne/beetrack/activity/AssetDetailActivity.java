package dne.beetrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dne.beetrack.R;
import dne.beetrack.daocontroller.AssetController;
import greendao.Asset;

/**
 * Created by loipn on 7/7/2016.
 */
public class AssetDetailActivity extends MyBaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtAssetName;
    private TextView txtName;
    private TextView txtId;
    private TextView txtSeri;
    private TextView txtWarehouseSeri;
    private TextView txtStatus;
    private TextView txtTime;
    private TextView txtNote;
    private RelativeLayout rltPrint;

    private long asset_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);

        if (getIntent().hasExtra("asset_id")) {
            asset_id = getIntent().getLongExtra("asset_id", 0);
            if (asset_id > 0) {
                initView();
                initData();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtAssetName = (TextView) findViewById(R.id.txtAssetName);
        txtName = (TextView) findViewById(R.id.txtName);
        txtId = (TextView) findViewById(R.id.txtId);
        txtSeri = (TextView) findViewById(R.id.txtSeri);
        txtWarehouseSeri = (TextView) findViewById(R.id.txtWarehouseSeri);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtNote = (TextView) findViewById(R.id.txtNote);
        rltPrint = (RelativeLayout) findViewById(R.id.rltPrint);

        rltBack.setOnClickListener(this);
        rltPrint.setOnClickListener(this);
    }

    private void initData() {
        Asset asset = AssetController.getById(this, asset_id);
        if (asset != null) {
            txtAssetName.setText(asset.getName());
            txtName.setText(asset.getName());
            txtId.setText(asset.getAsset_code());
            txtSeri.setText(asset.getSeri());
            txtWarehouseSeri.setText(asset.getWarehouse_seri());
            txtStatus.setText(asset.getF_active() == 1 ? "active" : "inactive");
            txtTime.setText(asset.getCreated_at());
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltPrint:
                Intent intent = new Intent(this, SamplePrintActivity.class);
                intent.putExtra("code", txtId.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
