package dne.beetrack.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.adapter.OptionAdapter;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.daocontroller.AssetController;
import dne.beetrack.daocontroller.DepartmentController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.daocontroller.WarehouseController;
import dne.beetrack.staticfunction.StaticFunction;
import greendao.Asset;
import greendao.Department;
import greendao.Session;
import greendao.Warehouse;

/**
 * Created by loipn on 7/7/2016.
 */
public class AssetDetailActivity extends MyBaseActivity implements View.OnClickListener {

    private int ASSET_NAME = 1;
    private int ASSET_CODE = 2;
    private int DEPARTMENT = 3;
    private int WAREHOUSE = 4;
    private int WAREHOUSE_SERI = 5;
    private int USER_USING = 6;
    private int SERI = 7;
    private int STATUS = 8;

    private RelativeLayout rltBack;
    private RelativeLayout rltPrint;

    private TextView txtName;
    private TextView txtAssetName;
    private TextView txtAssetCode;
    private TextView txtDepartment;
    private TextView txtWarehouse;
    private TextView txtWarehouseSeri;
    private TextView txtUserUsing;
    private TextView txtSeri;
    private TextView txtStatus;
    private ImageView imvAssetName;
    private ImageView imvAssetCode;
    private ImageView imvDepartment;
    private ImageView imvWarehouse;
    private ImageView imvWarehouseSeri;
    private ImageView imvUserUsing;
    private ImageView imvSeri;
    private ImageView imvStatus;

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
        rltPrint = (RelativeLayout) findViewById(R.id.rltPrint);
        txtName = (TextView) findViewById(R.id.txtName);
        txtAssetName = (TextView) findViewById(R.id.txtAssetName);
        txtAssetCode = (TextView) findViewById(R.id.txtAssetCode);
        txtDepartment = (TextView) findViewById(R.id.txtDepartment);
        txtWarehouse = (TextView) findViewById(R.id.txtWarehouse);
        txtWarehouseSeri = (TextView) findViewById(R.id.txtWarehouseSeri);
        txtUserUsing = (TextView) findViewById(R.id.txtUserUsing);
        txtSeri = (TextView) findViewById(R.id.txtSeri);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        imvAssetName = (ImageView) findViewById(R.id.imvAssetName);
        imvAssetCode = (ImageView) findViewById(R.id.imvAssetCode);
        imvDepartment = (ImageView) findViewById(R.id.imvDepartment);
        imvWarehouse = (ImageView) findViewById(R.id.imvWarehouse);
        imvWarehouseSeri = (ImageView) findViewById(R.id.imvWarehouseSeri);
        imvUserUsing = (ImageView) findViewById(R.id.imvUserUsing);
        imvSeri = (ImageView) findViewById(R.id.imvSeri);
        imvStatus = (ImageView) findViewById(R.id.imvStatus);

        rltBack.setOnClickListener(this);
        rltPrint.setOnClickListener(this);
        imvAssetName.setOnClickListener(this);
        imvAssetCode.setOnClickListener(this);
        imvDepartment.setOnClickListener(this);
        imvWarehouse.setOnClickListener(this);
        imvWarehouseSeri.setOnClickListener(this);
        imvUserUsing.setOnClickListener(this);
        imvSeri.setOnClickListener(this);
        imvStatus.setOnClickListener(this);
    }

    private void initData() {
        Asset asset = AssetController.getById(this, asset_id);
        if (asset != null) {
            txtName.setText(asset.getName());
            txtAssetName.setText(asset.getName());
            txtAssetCode.setText(asset.getAsset_code());
            txtDepartment.setText(asset.getDepartment_name());
            txtWarehouse.setText(asset.getWarehouse_name());
            txtWarehouseSeri.setText(asset.getWarehouse_seri());
            txtUserUsing.setText(asset.getUser_using());
            txtSeri.setText(asset.getSeri());
            txtStatus.setText(asset.getF_active() == 1 ? "Active" : "Inactive");
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
                intent.putExtra("code", AssetController.getById(this, asset_id).getAsset_code());
                startActivity(intent);
                break;
            case R.id.imvAssetName:
                showPopupEditText(getString(R.string.asset_name), ASSET_NAME);
                break;
            case R.id.imvAssetCode:
                showPopupEditText(getString(R.string.asset_code), ASSET_CODE);
                break;
            case R.id.imvDepartment:
                getListDepartment(getString(R.string.department), DEPARTMENT);
                break;
            case R.id.imvWarehouse:
                getListWarehouse(getString(R.string.warehouse), WAREHOUSE);
                break;
            case R.id.imvWarehouseSeri:
                showPopupEditText(getString(R.string.warehouse_seri), WAREHOUSE_SERI);
                break;
            case R.id.imvUserUsing:
                showPopupEditText(getString(R.string.user_using), USER_USING);
                break;
            case R.id.imvSeri:
                showPopupEditText(getString(R.string.seri), SERI);
                break;
            case R.id.imvStatus:
                showListStatus(getString(R.string.status), STATUS);
                break;
        }
    }

    public void showPopupEditText(String label, final int field) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_edit_text);

        TextView txtLabel = (TextView) dialog.findViewById(R.id.txtLabel);
        final EditText edtInput = (EditText) dialog.findViewById(R.id.edtInput);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtLabel.setText(label);
        String text = getFieldData(field);
        edtInput.setText(text);
        edtInput.setSelection(text.length(), text.length());

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAssetStringData(field, edtInput.getText().toString().trim());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getListDepartment(final String label, final int field) {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                hideProgressDialog();
                showListDepartment(label, field);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getListDepartment(this, callback, UserController.getCurrentUser(this).getCompany_id());
        showProgressDialog(false);
    }

    private void showListDepartment(String label, final int field) {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(label);
        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        List<String> stringList = new ArrayList<>();
        final List<Department> departmentList = DepartmentController.getAll(this);
        for (Department department : departmentList) {
            stringList.add(department.getDepartment_name());
        }

        OptionAdapter.Callback callback = new OptionAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                editAssetLongData(field, departmentList.get(position).getDepartment_id(), departmentList.get(position).getDepartment_name());
                dialog.dismiss();
            }
        };

        OptionAdapter adapter = new OptionAdapter(this, stringList, callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private void getListWarehouse(final String label, final int field) {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                hideProgressDialog();
                showListWarehouse(label, field);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getListWarehouse(this, callback, UserController.getCurrentUser(this).getCompany_id());
        showProgressDialog(false);
    }

    private void showListWarehouse(String label, final int field) {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(label);
        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        List<String> stringList = new ArrayList<>();
        final List<Warehouse> warehouseList = WarehouseController.getAll(this);
        for (Warehouse warehouse : warehouseList) {
            stringList.add(warehouse.getWarehouse_name());
        }

        OptionAdapter.Callback callback = new OptionAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                editAssetLongData(field, warehouseList.get(position).getWarehouse_id(), warehouseList.get(position).getWarehouse_name());
                dialog.dismiss();
            }
        };

        OptionAdapter adapter = new OptionAdapter(this, stringList, callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private void showListStatus(String label, final int field) {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(label);
        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        List<String> stringList = new ArrayList<>();
        stringList.add("Active");
        stringList.add("Inactive");

        OptionAdapter.Callback callback = new OptionAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                editAssetIsActive(field, position == 0 ? 1 : 0);
                dialog.dismiss();
            }
        };

        OptionAdapter adapter = new OptionAdapter(this, stringList, callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private String getFieldData(int field) {
        Asset asset = AssetController.getById(this, asset_id);
        switch (field) {
            case 1:
                return asset.getName();
            case 2:
                return asset.getAsset_code();
            case 3:
                return asset.getDepartment_name();
            case 4:
                return asset.getWarehouse_name();
            case 5:
                return asset.getWarehouse_seri();
            case 6:
                return asset.getUser_using();
            case 7:
                return asset.getSeri();
            case 8:
                return asset.getStatus() == 1 ? "Active" : "Inactive";
            default:
                return "";
        }
    }

    private String getFieldParam(int field) {
        switch (field) {
            case 1:
                return "name";
            case 2:
                return "asset_code";
            case 3:
                return "department_id";
            case 4:
                return "warehouse_id";
            case 5:
                return "warehouse_seri";
            case 6:
                return "user_using";
            case 7:
                return "seri";
            case 8:
                return "f_active";
            default:
                return "";
        }
    }

    private void editAssetStringData(final int field, final String data) {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                hideProgressDialog();
                Asset asset = AssetController.getById(AssetDetailActivity.this, asset_id);
                switch (field) {
                    case 1:
                        asset.setName(data);
                        break;
                    case 2:
                        asset.setAsset_code(data);
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        asset.setWarehouse_seri(data);
                        break;
                    case 6:
                        asset.setUser_using(data);
                        break;
                    case 7:
                        asset.setSeri(data);
                        break;
                    case 8:

                        break;
                }
                AssetController.insertOrUpdate(AssetDetailActivity.this, asset);
                initData();
                StaticFunction.sendBroadCast(AssetDetailActivity.this, StaticFunction.NOTIFY_LIST_ASSET);
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.editAsset(this, callback, asset_id, getFieldParam(field), data);
        showProgressDialog(false);
    }

    private void editAssetLongData(final int field, final long id, final String name) {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                hideProgressDialog();
                Asset asset = AssetController.getById(AssetDetailActivity.this, asset_id);
                switch (field) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:// long
                        asset.setDepartment_id(id);
                        asset.setDepartment_name(name);
                        break;
                    case 4:// long
                        asset.setWarehouse_id(id);
                        asset.setWarehouse_name(name);
                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:// int

                        break;
                }
                AssetController.insertOrUpdate(AssetDetailActivity.this, asset);
                initData();
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.editAsset(this, callback, asset_id, getFieldParam(field), id + "");
        showProgressDialog(false);
    }

    private void editAssetIsActive(final int field, final int f_active) {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                hideProgressDialog();
                Asset asset = AssetController.getById(AssetDetailActivity.this, asset_id);
                switch (field) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:// long

                        break;
                    case 4:// long

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:// int
                        asset.setF_active(f_active);
                        break;
                }
                AssetController.insertOrUpdate(AssetDetailActivity.this, asset);
                initData();
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.editAsset(this, callback, asset_id, getFieldParam(field), f_active + "");
        showProgressDialog(false);
    }
}
