package com.phoenix.mvcdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.phoenix.mvcdemo.R;
import com.phoenix.mvcdemo.model.UserEntity;

public class MVCActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private EditText etUserName;
    private EditText etPassword;
    private Button btn;
    private String result;
    private UserEntity mUserEntity;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login(etUserName.getText().toString(), etPassword.getText().toString());
                if(!TextUtils.isEmpty(result)){
                    mUserEntity = new Gson().fromJson(result,UserEntity.class);
                    tvResult.setText(mUserEntity.address);
                }
            }
        });
    }

    private void login(String userName, String pwd) {
        if(TextUtils.isEmpty(userName)){
            etUserName.setError("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            etPassword.setError("密码不能为空");
            return;
        }
        if(pwd.equals("abc")){
            /*{
                "nickName": "phoenix",
                "userID": "1",
                "address": "吉林吉林"
            }*/
            result = "{\n" +
                    "    \"nickName\": \"phoenix\",\n" +
                    "    \"userID\": \"1\",\n" +
                    "    \"address\": \"吉林吉林\"\n" +
                    "}";
        }else {
            Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
        }
    }

}
