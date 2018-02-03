package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.User;
import com.jpble.presenter.LoginPresenterImp;
import com.jpble.utils.DeviceUuidFactory;
import com.jpble.utils.MD5;
import com.jpble.utils.SpUtils;
import com.jpble.view.LoginView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LoginView {


    @BindView(R.id.main_ll)
    LinearLayout mainLl;
    @BindView(R.id.mian_mail)
    EditText mianMail;
    @BindView(R.id.mian_psw)
    EditText mianPsw;

    LoginPresenterImp loginPresenterImp;
    ProgressDialog progressDialog;


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));

        //setHideAnimation(2000);
        loginPresenterImp = new LoginPresenterImp(this, this);
    }


    @OnClick({R.id.mian_sign, R.id.main_login, R.id.main_facebook, R.id.main_twitter, R.id.mian_forgot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mian_sign:
                // startActivity(new Intent(this, HomeActivity.class));
                Map<String, String> map = new HashMap<>();
                String phone = mianMail.getText().toString().trim();
                String psw = mianPsw.getText().toString().trim();
                String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
                if (isEmail(phone) && !TextUtils.isEmpty(psw)) {
                    String token = SpUtils.getString("googleToken", "");
                    map.put("account", phone);
                    map.put("password", MD5.getMD5(psw));
                    map.put("deviceUUID", uuid);
                    map.put("deviceToken", token);
                    map.put("deviceType", "1");
                    loginPresenterImp.loadLogin(map);
                } else {
                    showToastor(getString(R.string.login_msg8));
                }
                break;
            case R.id.main_login:
                startActivity(new Intent(this, RegisterActivity.class).putExtra("type", 1));
                break;
            case R.id.mian_forgot:
                startActivity(new Intent(this, RegisterActivity.class).putExtra("type", 0));
                break;
            case R.id.main_facebook:
                break;
            case R.id.main_twitter:
                break;
            default:
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void disimissProgress() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(User tData) {
        String phone = mianMail.getText().toString().trim();
        String psw = mianPsw.getText().toString().trim();
        if (tData.getCode() == 200) {
            showToastor(getString(R.string.login_msg22));
            MyApplication.newInstance().setUser(tData);
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            if (!phone.isEmpty()) {
                SpUtils.putString("phone", phone);
                SpUtils.putString("psw", psw);
            }
            finish();
        } else {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            err(tData.getCode());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String phone = SpUtils.getString("phone", "");
        String psw = SpUtils.getString("psw", "");
        String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
        String token = SpUtils.getString("googleToken", "");
        if (isEmail(phone) && !TextUtils.isEmpty(psw)) {
            Map<String, String> map = new HashMap<>();
            map.put("account", phone);
            map.put("password", MD5.getMD5(psw));
            map.put("deviceUUID", uuid);
            map.put("deviceToken", token);
            map.put("deviceType", "1");
            loginPresenterImp.loadLogin(map);
        }
    }
}
