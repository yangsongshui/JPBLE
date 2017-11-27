package com.jpble.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.AboutActivity;
import com.jpble.activity.BluetoothDeviceActivity;
import com.jpble.activity.DeviceManageActivity;
import com.jpble.activity.EquipmentActivity;
import com.jpble.activity.SimManageActivity;
import com.jpble.activity.TripActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.ToHex;

import butterknife.BindView;
import butterknife.OnClick;


public class MeFragment extends BaseFragment {

    @BindView(R.id.me_pic_iv)
    ImageView mePicIv;
    @BindView(R.id.me_name_tv)
    TextView meNameTv;
    @BindView(R.id.me_address_tv)
    TextView meAddressTv;
    @BindView(R.id.me_email_tv)
    TextView meEmailTv;
    LinkBLE linkBLE;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        linkBLE = MyApplication.newInstance().getBleManager();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_me;
    }

    @OnClick({R.id.me_trip, R.id.me_modify, R.id.me_equipment, R.id.me_about, R.id.me_bluetooth, R.id.me_sim, R.id.me_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_trip:
                startActivity(new Intent(getActivity(), TripActivity.class));
                break;
            case R.id.me_modify:
                String msg = MyApplication.newInstance().KEY + "3100";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                break;
            case R.id.me_equipment:
                startActivity(new Intent(getActivity(), EquipmentActivity.class));
                break;
            case R.id.me_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.me_bluetooth:
                startActivity(new Intent(getActivity(), BluetoothDeviceActivity.class));
                break;
            case R.id.me_sim:
                startActivity(new Intent(getActivity(), SimManageActivity.class));
                break;
            case R.id.me_device:
                startActivity(new Intent(getActivity(), DeviceManageActivity.class));
                break;
            default:
                break;
        }
    }
}
