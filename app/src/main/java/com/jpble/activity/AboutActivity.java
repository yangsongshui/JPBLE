package com.jpble.activity;

import android.widget.TextView;

import com.jpble.R;
import com.jpble.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity   {

    @BindView(R.id.about_msg)
    TextView aboutMsg;
    @BindView(R.id.about_msg2)
    TextView aboutMsg2;


    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        aboutMsg.setText(String.format(getString(R.string.about_msg), "1.2.2"));
        aboutMsg2.setText(String.format(getString(R.string.about_msg2), "1.1.1"));

    }


    @OnClick(R.id.about_return)
    public void onViewClicked() {
        finish();
    }

}
