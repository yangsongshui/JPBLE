package com.jpble.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LoginView {
    private final static int REQUECT_CODE_COARSE = 1;
    @BindView(R.id.main_logo)
    ImageView mainLogo;
    @BindView(R.id.main_ll)
    LinearLayout mainLl;
    @BindView(R.id.mian_mail)
    EditText mianMail;
    @BindView(R.id.mian_psw)
    EditText mianPsw;
    Animation mHideAnimation;
    Animation mShowAnimation;
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
        initPermission();
        setHideAnimation(2000);
        loginPresenterImp = new LoginPresenterImp(this, this);
    }


    @OnClick({R.id.mian_sign, R.id.main_login, R.id.main_facebook, R.id.main_twitter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mian_sign:
                  startActivity(new Intent(this, HomeActivity.class));
          /*      Map<String, String> map = new HashMap<>();
                String phone = mianMail.getText().toString().trim();
                String psw = mianPsw.getText().toString().trim();
                String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
                if (isEmail(phone) && !TextUtils.isEmpty(psw)) {
                    map.put("account", phone);
                    map.put("password", MD5.getMD5(psw));
                    map.put("deviceUUID", uuid);
                    loginPresenterImp.loadLogin(map);
                } else {
                    showToastor(getString(R.string.login_msg8));
                }*/
                break;
            case R.id.main_login:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.main_facebook:
                break;
            case R.id.main_twitter:
                break;
            default:
                break;
        }
    }

    public void setHideAnimation(int duration) {
        if (duration < 0) {
            return;
        }

        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mainLogo.startAnimation(mHideAnimation);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainLogo.setVisibility(View.GONE);
                mainLl.setVisibility(View.VISIBLE);
                setShowAnimation(500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setShowAnimation(int duration) {
        if (duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mainLl.startAnimation(mShowAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
    }


    private void initPermission() {
        MPermissions.requestPermissions(this, REQUECT_CODE_COARSE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(REQUECT_CODE_COARSE)
    public void requestSdcardSuccess() {
    }

    @PermissionDenied(REQUECT_CODE_COARSE)
    public void requestSdcardFailed() {
        MPermissions.requestPermissions(this, REQUECT_CODE_COARSE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void disimissProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public void loadDataSuccess(User tData) {
        String phone = mianMail.getText().toString().trim();
        String psw = mianPsw.getText().toString().trim();
        if (tData.getCode() == 200) {
            showToastor(getString(R.string.login_msg22));
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            MyApplication.newInstance().setUser(tData);
            if (!phone.isEmpty()) {
                SpUtils.putString("phone", phone);
                SpUtils.putString("psw", psw);
            }
            finish();
        } else {
            err(tData.getCode());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String phone = SpUtils.getString("phone", "");
        String psw = SpUtils.getString("psw", "");
        String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
        if (isEmail(phone) && !TextUtils.isEmpty(psw)) {
            Map<String, String> map = new HashMap<>();
            map.put("account", phone);
            map.put("password", MD5.getMD5(psw));
            map.put("deviceUUID", uuid);
            loginPresenterImp.loadLogin(map);
        }
    }
}
