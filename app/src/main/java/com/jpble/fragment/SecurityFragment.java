package com.jpble.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.MyAssetActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.SECURITY_SWITCH;


public class SecurityFragment extends BaseFragment {


    @BindView(R.id.security_schema)
    ImageView securitySchema;
    @BindView(R.id.security_electricity)
    ImageView securityElectricity;
    @BindView(R.id.security_dianliang)
    TextView securityDianliang;
    LinkBLE linkBLE;
    boolean security = false;

    public SecurityFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        security = SpUtils.getBoolean(SECURITY_SWITCH, false);
        linkBLE = MyApplication.newInstance().getBleManager();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_security;
    }


    @OnClick({R.id.main_list, R.id.security_search, R.id.security_message, R.id.security_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_list:
                startActivity(new Intent(getActivity(), MyAssetActivity.class));
                break;
            case R.id.security_search:
                break;
            case R.id.security_message:
                security = !security;
                String msg = MyApplication.newInstance().KEY + "1207" + (security ? "01" : "02") + "00000000";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                SpUtils.putBoolean(SECURITY_SWITCH, security);
                break;
            case R.id.security_news:
                break;
            default:
                break;
        }
    }
}
