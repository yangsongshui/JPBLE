package com.jpble.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.AssetActivity;
import com.jpble.activity.DeviceManageActivity;
import com.jpble.activity.EquipmentActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.DeviceState;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.SpUtils;
import com.jpble.utils.Toastor;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.EXTRA_STATUS;


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
    Toastor toastor;
    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        linkBLE = MyApplication.newInstance().getBleManager();
        toastor=new Toastor(getActivity());

        PackageManager pm = getActivity().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            aboutMsg.setText(String.format(getString(R.string.about_msg), packageInfo.versionName));
            aboutMsg2.setText(String.format(getString(R.string.about_msg2), "1.0"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EXTRA_STATUS);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        getActivity().registerReceiver(notifyReceiver, intentFilter);
        meEmailTv.setText(SpUtils.getString("phone", ""));
        meNameTv.setText(SpUtils.getString("name", "name"));

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_me;
    }

    @OnClick({R.id.me_asset, R.id.me_modify, R.id.me_equipment, R.id.me_contact, R.id.me_out, R.id.me_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_asset:
                startActivity(new Intent(getActivity(), AssetActivity.class));
                break;
            case R.id.me_modify:
                showDialog();
                break;
            case R.id.me_equipment:
                startActivity(new Intent(getActivity(), EquipmentActivity.class));
                break;
            case R.id.me_out:
                //退出登录
                SpUtils.putString("phone", "");
                SpUtils.putString("psw", "");
                getActivity().finish();
                break;
            case R.id.me_device:
                if (linkBLE.isLink())
                    startActivity(new Intent(getActivity(), DeviceManageActivity.class));
                else
                    toastor.showSingletonToast(getActivity().getString(R.string.toastor_msg5));
                break;
            case R.id.me_contact:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://geo.crops-sports.com/contact/index.html");
                intent.setData(content_url);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BluetoothActivity", intent.getAction());
            //设备
            if (EXTRA_STATUS.equals(intent.getAction())) {
                //收到设备信息
                DeviceState deviceState = (DeviceState) intent.getSerializableExtra(EXTRA_STATUS);
                String v = deviceState.getBanben() + "." + (deviceState.getBanben2().trim().isEmpty() ? "0" : deviceState.getBanben2());
                aboutMsg2.setText(String.format(getString(R.string.about_msg2), v));
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(notifyReceiver);
    }

    private void showDialog() {
        final EditText et = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle(getActivity().getString(R.string.name))
                .setView(et)
                .setPositiveButton(getActivity().getString(R.string.activity_dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (!input.equals("")) {
                            meNameTv.setText(input);
                            SpUtils.putString("name", input);
                        }
                    }
                })
                .setNegativeButton(getActivity().getString(R.string.activity_dialog_cancel), null)
                .show();
    }
}
