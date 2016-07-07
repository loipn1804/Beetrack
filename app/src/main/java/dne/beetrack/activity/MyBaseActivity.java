package dne.beetrack.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import dne.beetrack.R;
import dne.beetrack.staticfunction.StaticFunction;
import dne.beetrack.view.SimpleToast;

/**
 * Created by USER on 3/3/2016.
 */
public class MyBaseActivity extends AppCompatActivity {

    private Dialog progress_dialog = null;
    private boolean isDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    protected boolean isDestroy() {
        return isDestroy;
    }

    public void showProgressDialog(boolean cancelable) {
        if (progress_dialog == null) {
            progress_dialog = new Dialog(MyBaseActivity.this);
            progress_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progress_dialog.setContentView(R.layout.progress_dialog);
        }

        if (!progress_dialog.isShowing()) {
            progress_dialog.setCanceledOnTouchOutside(cancelable);
            progress_dialog.setCancelable(cancelable);
            progress_dialog.show();
        }

        if (!StaticFunction.isNetworkAvailable(this)) {
            progress_dialog.dismiss();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

    public void showToastError(String message) {
        if (message != null) {
            SimpleToast.error(MyBaseActivity.this, message);
        }
    }

    public void showToastInfo(String message) {
        if (message != null) {
            SimpleToast.info(MyBaseActivity.this, message);
        }
    }

    public void showToastOk(String message) {
        if (message != null) {
            SimpleToast.ok(MyBaseActivity.this, message);
        }
    }
}
