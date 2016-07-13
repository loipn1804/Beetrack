package dne.beetrack.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dne.beetrack.R;
import dne.beetrack.adapter.OptionAdapter;
import dne.beetrack.adapter.SessionAdapter;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.daocontroller.CategoryController;
import dne.beetrack.daocontroller.DepartmentController;
import dne.beetrack.daocontroller.SessionController;
import dne.beetrack.daocontroller.SubCategoryController;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.staticfunction.StaticFunction;
import dne.beetrack.view.MyCheckbox;
import greendao.Category;
import greendao.Department;
import greendao.Session;
import greendao.SubCategory;

/**
 * Created by loipn on 7/12/2016.
 */
public class FilterActivity extends MyBaseActivity implements View.OnClickListener {

    private MyCheckbox btnScanned;
    private MyCheckbox btnNotScanYet;
    private LinearLayout lnlDepartment;
    private TextView txtDepartment;
    private LinearLayout lnlCategory;
    private TextView txtCategory;
    private LinearLayout lnlSubCategory;
    private TextView txtSubCategory;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initView();
        initData();
    }

    private void initView() {
        btnScanned = (MyCheckbox) findViewById(R.id.btnScanned);
        btnNotScanYet = (MyCheckbox) findViewById(R.id.btnNotScanYet);
        lnlDepartment = (LinearLayout) findViewById(R.id.lnlDepartment);
        txtDepartment = (TextView) findViewById(R.id.txtDepartment);
        lnlCategory = (LinearLayout) findViewById(R.id.lnlCategory);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        lnlSubCategory = (LinearLayout) findViewById(R.id.lnlSubCategory);
        txtSubCategory = (TextView) findViewById(R.id.txtSubCategory);
        btnOK = (Button) findViewById(R.id.btnOK);

        btnScanned.setOnClickListener(this);
        btnNotScanYet.setOnClickListener(this);
        lnlDepartment.setOnClickListener(this);
        lnlCategory.setOnClickListener(this);
        lnlSubCategory.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    private void initData() {
        btnScanned.setIsChecked(getFilterScanned());
        btnNotScanYet.setIsChecked(getFilterNotScanYet());

        getListDepartment();
    }

    private void setData() {
        Category category = CategoryController.getByCategoryId(FilterActivity.this, getFilterCategory());
        if (category == null) {
            txtCategory.setText(getString(R.string.all));
        } else {
            txtCategory.setText(category.getCategory_name());
        }

        SubCategory subCategory = SubCategoryController.getBySubCategoryId(FilterActivity.this, getFilterSubCategory());
        if (subCategory == null) {
            txtSubCategory.setText(getString(R.string.all));
        } else {
            txtSubCategory.setText(subCategory.getSub_category_name());
        }

        Department department = DepartmentController.getByDepartmentId(FilterActivity.this, getFilterDepartment());
        if (department == null) {
            txtDepartment.setText(getString(R.string.all));
        } else {
            txtDepartment.setText(department.getDepartment_name());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanned:
                btnScanned.toggleCheck();
                setFilterScanned(btnScanned.getIsChecked());
                break;
            case R.id.btnNotScanYet:
                btnNotScanYet.toggleCheck();
                setFilterNotScanYet(btnNotScanYet.getIsChecked());
                break;
            case R.id.lnlDepartment:
                showListDepartment();
                break;
            case R.id.lnlCategory:
                showListCategory();
                break;
            case R.id.lnlSubCategory:
                showListSubCategory();
                break;
            case R.id.btnOK:
                finish();
                break;
        }
    }

    private void getListDepartment() {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
//                hideProgressDialog();
//                setData();
                getListCategory();
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

    private void getListCategory() {
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                hideProgressDialog();
                setData();
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.getListCategory(this, callback, UserController.getCurrentUser(this).getCompany_id());
//        showProgressDialog(false);
    }

    private void showListDepartment() {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(getString(R.string.choose_session));
        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        List<String> stringList = new ArrayList<>();
        final List<Department> departmentList = DepartmentController.getAll(this);
        departmentList.add(0, new Department(0l, "0", getString(R.string.all), ""));
        for (Department department : departmentList) {
            stringList.add(department.getDepartment_name());
        }

        OptionAdapter.Callback callback = new OptionAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                setFilterDepartment(departmentList.get(position).getDepartment_id());
                setData();
                dialog.dismiss();
            }
        };

        OptionAdapter adapter = new OptionAdapter(this, stringList, callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private void showListCategory() {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(getString(R.string.choose_session));
        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        List<String> stringList = new ArrayList<>();
        final List<Category> categoryList = CategoryController.getAll(this);
        categoryList.add(0, new Category(0l, getString(R.string.all)));
        for (Category category : categoryList) {
            stringList.add(category.getCategory_name());
        }

        OptionAdapter.Callback callback = new OptionAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                setFilterCategory(categoryList.get(position).getCategory_id());
                setFilterSubCategory(0);
                setData();
                dialog.dismiss();
            }
        };

        OptionAdapter adapter = new OptionAdapter(this, stringList, callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private void showListSubCategory() {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(getString(R.string.choose_session));
        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        List<String> stringList = new ArrayList<>();
        final List<SubCategory> subCategoryList = SubCategoryController.getAllByCategoryId(this, getFilterCategory());
        subCategoryList.add(0, new SubCategory(0l, 0l, getString(R.string.all)));
        for (SubCategory subCategory : subCategoryList) {
            stringList.add(subCategory.getSub_category_name());
        }

        OptionAdapter.Callback callback = new OptionAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                setFilterSubCategory(subCategoryList.get(position).getSub_category_id());
                setData();
                dialog.dismiss();
            }
        };

        OptionAdapter adapter = new OptionAdapter(this, stringList, callback);
        listView.setAdapter(adapter);

        dialog.show();
    }

    private long getFilterCategory() {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        return preferences.getLong("category_id", 0);
    }

    private void setFilterCategory(long category_id) {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("category_id", category_id);
        editor.commit();
        StaticFunction.sendBroadCast(FilterActivity.this, StaticFunction.NOTIFY_LIST_ASSET);
    }

    private long getFilterSubCategory() {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        return preferences.getLong("sub_category_id", 0);
    }

    private void setFilterSubCategory(long sub_category_id) {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("sub_category_id", sub_category_id);
        editor.commit();
        StaticFunction.sendBroadCast(FilterActivity.this, StaticFunction.NOTIFY_LIST_ASSET);
    }

    private long getFilterDepartment() {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        return preferences.getLong("department_id", 0);
    }

    private void setFilterDepartment(long department_id) {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("department_id", department_id);
        editor.commit();
        StaticFunction.sendBroadCast(FilterActivity.this, StaticFunction.NOTIFY_LIST_ASSET);
    }

    private boolean getFilterScanned() {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        return preferences.getBoolean("scanned", true);
    }

    private void setFilterScanned(boolean scanned) {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("scanned", scanned);
        editor.commit();
        StaticFunction.sendBroadCast(FilterActivity.this, StaticFunction.NOTIFY_LIST_ASSET);
    }

    private boolean getFilterNotScanYet() {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        return preferences.getBoolean("not_scan_yet", true);
    }

    private void setFilterNotScanYet(boolean not_scan_yet) {
        SharedPreferences preferences = getSharedPreferences("filter", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("not_scan_yet", not_scan_yet);
        editor.commit();
        StaticFunction.sendBroadCast(FilterActivity.this, StaticFunction.NOTIFY_LIST_ASSET);
    }
}
