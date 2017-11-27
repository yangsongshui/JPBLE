package com.jpble.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;

import butterknife.BindView;

import static com.jpble.utils.Constant.LOCK_STATUS;
import static com.jpble.utils.Constant.VIBRATION_SWITCH;


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
    LinkBLE linkBLE;

    public SecurityFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        initView();
        securityPowerCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String key = MyApplication.newInstance().KEY + "12070000" + (isChecked ? "01" : "02") + "00000000";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
                SpUtils.putBoolean(VIBRATION_SWITCH, isChecked);
                //linkBLE.write(Constant.jiami("FE", ToHex.random(), "2109000000000000000001")); //开锁指令

            }
        });
        securityAutoCb.setOnClickListener(this);
        securityOnCb.setOnClickListener(this);
        securityOffCb.setOnClickListener(this);
        linkBLE = MyApplication.newInstance().getBleManager();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_security;
    }


    @Override
    public void onClick(View v) {
        String security = "";
        switch (v.getId()) {
            case R.id.security_auto_cb:
                security = "01";
                break;
            case R.id.security_on_cb:
                security = "02";
                break;
            case R.id.security_off_cb:
                security = "03";
                break;
            default:
                break;
        }

        securityAutoIv.setVisibility(v.getId() == R.id.security_auto_cb ? View.VISIBLE : View.GONE);
        securityOnIv.setVisibility(v.getId() == R.id.security_on_cb ? View.VISIBLE : View.GONE);
        securityOffIv.setVisibility(v.getId() == R.id.security_off_cb ? View.VISIBLE : View.GONE);
        securityAutoCb.setTextColor(getResources().getColor(v.getId() == R.id.security_auto_cb ? R.color.blue3 : R.color.black));
        securityOnCb.setTextColor(getResources().getColor(v.getId() == R.id.security_on_cb ? R.color.blue3 : R.color.black));
        securityOffCb.setTextColor(getResources().getColor(v.getId() == R.id.security_off_cb ? R.color.blue3 : R.color.black));
        SpUtils.putString(LOCK_STATUS, security);
        String key = MyApplication.newInstance().KEY + "1207" + security + "000000000000";
        linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
    }

    private void initView() {
        String security = SpUtils.getString(LOCK_STATUS, "01");
        securityAutoIv.setVisibility(security.equals("01") ? View.VISIBLE : View.GONE);
        securityOnIv.setVisibility(security.equals("02") ? View.VISIBLE : View.GONE);
        securityOffIv.setVisibility(security.equals("03") ? View.VISIBLE : View.GONE);
        securityAutoCb.setTextColor(getResources().getColor(security.equals("01") ? R.color.blue3 : R.color.black));
        securityOnCb.setTextColor(getResources().getColor(security.equals("02") ? R.color.blue3 : R.color.black));
        securityOffCb.setTextColor(getResources().getColor(security.equals("03") ? R.color.blue3 : R.color.black));
        securityPowerCb.setChecked(SpUtils.getBoolean(VIBRATION_SWITCH, false));
    }
}
