package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jpble.R;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Code;
import com.jpble.presenter.RegiserPresenterImp;
import com.jpble.utils.MD5;
import com.jpble.utils.SpUtils;
import com.jpble.view.CodeView;
import com.jpble.widget.VerificationCodeInput;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.BASE_URL;

public class CodeActivity extends BaseActivity implements CodeView {
    private static final String TAG = CodeActivity.class.getName();
    @BindView(R.id.code_msg_ll)
    LinearLayout codeMsgLl;
    @BindView(R.id.verificationCodeInput)
    VerificationCodeInput verificationCodeInput;
    String code;
    String psw;
    String mail;
    RegiserPresenterImp regiserPresenterImp;
    ProgressDialog progressDialog;
    int type = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_code;
    }

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type", 0);
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.login_msg7));
        psw = getIntent().getStringExtra("psw");
        mail = getIntent().getStringExtra("mail");
        regiserPresenterImp = new RegiserPresenterImp(this, this);
        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                Log.d(TAG, "完成输入：" + content);
                code = content;
            }
        });
    }


    @OnClick({R.id.code_back, R.id.code_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.code_back:
                finish();
                break;
            case R.id.code_next:
                if (code.length() == 6)
                    reguster();
                else
                    showToastor(getString(R.string.code_msg1));
                break;
            default:
                break;
        }
    }

    private void reguster() {
        Map<String, String> map = new HashMap<>();
        String url;
        if (type == 1) {
            url = BASE_URL + "register/10001/" + code;
        } else {
            url = BASE_URL + "register/10002/" + code;
        }

        map.put("code", code);
        map.put("email", mail);
        map.put("password", MD5.getMD5(psw));
        regiserPresenterImp.register(url, map);
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
    public void loadDataSuccess(Code tData) {
        if (tData.getCode() == 200) {
            if (type == 1) {
                showToastor(getString(R.string.login_msg9));
                SpUtils.putString("phone", mail);
                SpUtils.putString("psw", psw);
                startActivity(new Intent(this, SucceedActivity.class));
                finish();
            } else {
                showToastor(getString(R.string.login_msg23));
                finish();
            }

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
    protected void onDestroy() {
        super.onDestroy();
        regiserPresenterImp.unSubscribe();
    }
}
