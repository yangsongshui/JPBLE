package com.jpble.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.jpble.R;
import com.jpble.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {

    @BindView(R.id.sign_mail)
    EditText signMail;
    @BindView(R.id.sign_psw)
    EditText signPsw;

    @Override
    protected int getContentView() {
        return R.layout.activity_sign;
    }

    @Override
    protected void init() {

    }


    @OnClick({R.id.sign_return, R.id.sign_forgot, R.id.sign_in, R.id.sign_facebook, R.id.sign_twitter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_return:
                finish();
                break;
            case R.id.sign_forgot:
                break;
            case R.id.sign_in:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.sign_facebook:
                break;
            case R.id.sign_twitter:
                break;
            default:
                break;
        }
    }
}
