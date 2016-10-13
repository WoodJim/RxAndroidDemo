package com.wusj.rxjavademo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by wusj on 2016/10/12.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userNameEditText;
    EditText userPwdEditText;
    Button loginBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

//        Android特有的几个Observable:
//        * AppObservable,ViewObservable,WidgetObservable,LifecycleObservable

    }

    private void initView() {
        userNameEditText = (EditText) findViewById(R.id.et_user_name);
        userPwdEditText = (EditText) findViewById(R.id.et_user_pwd);
        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        init();
    }

    private void init() {
//        AppObservable

    }

    @Override
    public void onClick(View view) {

    }
}
