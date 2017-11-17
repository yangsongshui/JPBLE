package com.jpble.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.base.BaseFragment;

import butterknife.BindView;


public class SecurityFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.security_power_cb)
    CheckBox securityPowerCb;
    @BindView(R.id.security_auto_cb)
    TextView securityAutoCb;
    @BindView(R.id.security_auto_iv)
    ImageView securityAutoIv;
    @BindView(R.id.security_on_cb)
    TextView securityOnCb;
    @BindView(R.id.security_on_iv)
    ImageView securityOnIv;
    @BindView(R.id.security_off_cb)
    TextView securityOffCb;
    @BindView(R.id.security_off_iv)
    ImageView securityOffIv;

    public SecurityFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, Bundle savedInstanceState) {
        securityPowerCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        securityAutoCb.setOnClickListener(this);
        securityOnCb.setOnClickListener(this);
        securityOffCb.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_security;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_auto_cb:

                break;
            case R.id.security_on_cb:

                break;
            case R.id.security_off_cb:

                break;
            default:
                break;
        }
        securityAutoIv.setVisibility(v.getId()==R.id.security_auto_cb?View.VISIBLE:View.GONE);
        securityOnIv.setVisibility(v.getId()==R.id.security_on_cb?View.VISIBLE:View.GONE);
        securityOffIv.setVisibility(v.getId()==R.id.security_off_cb?View.VISIBLE:View.GONE);
        securityAutoCb.setTextColor(getResources().getColor(v.getId()==R.id.security_auto_cb?R.color.blue3:R.color.black));
        securityOnCb.setTextColor(getResources().getColor(v.getId()==R.id.security_on_cb?R.color.blue3:R.color.black));
        securityOffCb.setTextColor(getResources().getColor(v.getId()==R.id.security_off_cb?R.color.blue3:R.color.black));
    }
}
