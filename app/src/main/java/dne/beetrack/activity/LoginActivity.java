package dne.beetrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import dne.beetrack.R;

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

    private void login() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
