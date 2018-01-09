package com.jpble.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.AssetActivity;
import com.jpble.activity.DeviceManageActivity;
import com.jpble.activity.EquipmentActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.LinkBLE;

import butterknife.BindView;
import butterknife.OnClick;


public class MeFragment extends BaseFragment {

    @BindView(R.id.me_name_tv)
    TextView meNameTv;

    @BindView(R.id.me_email_tv)
    TextView meEmailTv;
    @BindView(R.id.about_msg)
    TextView aboutMsg;
    @BindView(R.id.about_msg2)
    TextView aboutMsg2;
    LinkBLE linkBLE;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        linkBLE = MyApplication.newInstance().getBleManager();
        aboutMsg.setText(String.format(getString(R.string.about_msg), "1.2.2"));
        aboutMsg2.setText(String.format(getString(R.string.about_msg2), "1.1.1"));
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_me;
    }

    @OnClick({R.id.me_asset, R.id.me_modify, R.id.me_equipment, R.id.me_out ,R.id.me_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_asset:
                startActivity(new Intent(getActivity(), AssetActivity.class));
                break;
            case R.id.me_modify:
               /* String msg = MyApplication.newInstance().KEY + "3100";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));*/
                break;
            case R.id.me_equipment:
                startActivity(new Intent(getActivity(), EquipmentActivity.class));
                break;
            case R.id.me_out:
                //退出登录
                break;
            case R.id.me_device:
                startActivity(new Intent(getActivity(), DeviceManageActivity.class));
                break;
            default:
                break;
        }
    }
}
