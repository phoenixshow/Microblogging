package com.phoenix.mvcdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.phoenix.mvcdemo.R;
import com.phoenix.mvcdemo.presenter.LoginPresenter;
import com.phoenix.mvcdemo.presenter.LoginPresenterImp;
import com.phoenix.mvcdemo.view.LoginView;

public class MainActivity extends AppCompatActivity implements LoginView {

    private Toolbar toolbar;
    private EditText etUserName;
    private EditText etPassword;
    private Button btn;
    private TextView tvResult;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mPresenter = new LoginPresenterImp(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPresenter.login();
            }
        });
    }

    public String getUerName() {
        return etUserName.getText().toString();
    }

    public String getPwd() {
        return etPassword.getText().toString();
    }

    public void onUserNameError() {
        etUserName.setError("用户名不能为空");
    }

    public void onPwdError() {
        etPassword.setError("密码不能为空");
    }

    public void onError() {
        Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
    }

//    public void onSuccess(UserEntity entity) {
//        tvResult.setText(entity.address);
//    }
    public void onSuccess(String address) {
        tvResult.setText(address);
    }

}
