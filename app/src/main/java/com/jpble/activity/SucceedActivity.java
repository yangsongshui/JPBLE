package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.User;
import com.jpble.presenter.LoginPresenterImp;
import com.jpble.utils.DeviceUuidFactory;
import com.jpble.utils.MD5;
import com.jpble.utils.SpUtils;
import com.jpble.view.LoginView;
import com.jpble.widget.HintDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

import static com.jpble.R.style.dialog;

/**
 * @author omni20170501
 */
public class SucceedActivity extends BaseActivity implements View.OnClickListener, LoginView {
    HintDialog hintDialog;
    LoginPresenterImp loginPresenterImp;
    ProgressDialog progressDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_succeed;
    }

    @Override
    protected void init() {
        String phone = SpUtils.getString("phone", "");
        String psw = SpUtils.getString("psw", "");
        hintDialog = new HintDialog(this, dialog);

        hintDialog.setOnClickListener(this);
        hintDialog.setCancelable(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        loginPresenterImp = new LoginPresenterImp(this, this);
        String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
        if (isEmail(phone) && !TextUtils.isEmpty(psw)) {
            Map<String, String> map = new HashMap<>();
            map.put("account", phone);
            map.put("password", MD5.getMD5(psw));
            map.put("deviceUUID", uuid);
            loginPresenterImp.loadLogin(map);

        }

    }

    @OnClick({R.id.succeed_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.succeed_back:
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hint_cancel:
                startActivity(new Intent(this, AssetActivity.class));
                finish();
                break;
            case R.id.hint_confirm:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;
            default:
                break;
        }
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
        if (tData.getCode() == 200) {
            MyApplication.newInstance().setUser(tData);
            hintDialog.show();
        } else {
            err(tData.getCode());

        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        showToastor(getString(R.string.login_msg10));
    }
}
