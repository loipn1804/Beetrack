package dne.beetrack.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import dne.beetrack.R;
import dne.beetrack.connection.Executor;
import dne.beetrack.connection.callback.UICallback;
import dne.beetrack.daocontroller.UserController;
import dne.beetrack.view.SimpleToast;
import greendao.User;

/**
 * Created by USER on 06/20/2016.
 */
public class LoginActivity extends MyBaseActivity implements View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPassword;
    private TextView btnLogin;
    private TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
    }

    private void initView() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        btnLogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        login();
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        if (UserController.isLogin(this)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
            String email = preferences.getString("email", "");
            edtEmail.setText(email);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.txtForgotPassword:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void login() {
        final String username = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (username.isEmpty()) {
            showToastError(getString(R.string.blank_username));
            return;
        } else if (password.isEmpty()) {
            showToastError(getString(R.string.blank_password));
            return;
        }
        UICallback callback = new UICallback() {
            @Override
            public void onSuccess(String message) {
                showToastOk(message);
                hideProgressDialog();
                saveEmail(username);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        };
        Executor.login(this, callback, username, password);
        showProgressDialog(false);
    }

    private void saveEmail(String email) {
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
