package com.jpble.fragment;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.LogActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.bean.Code;
import com.jpble.ble.DeviceState;
import com.jpble.ble.LinkBLE;
import com.jpble.presenter.SecurityPresenterImp;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.view.CodeView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.EXTRA_STATUS;
import static com.jpble.utils.Constant.SECURITY_SWITCH;


public class SecurityFragment extends BaseFragment implements CodeView {


    @BindView(R.id.security_schema)
    ImageView securitySchema;
    @BindView(R.id.security_electricity)
    ImageView securityElectricity;

    @BindView(R.id.security_message)
    CheckBox securityMessage;

    @BindView(R.id.security_name)
    TextView security_name;
    LinkBLE linkBLE;
    SecurityPresenterImp securityPresenterImp;
    Toastor toastor;
    ProgressDialog progressDialog;
    Handler handler;
    Runnable runnable;
    boolean one = true;

    public SecurityFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        linkBLE = MyApplication.newInstance().getBleManager();
        handler = new Handler();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EXTRA_STATUS);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        getActivity().registerReceiver(notifyReceiver, intentFilter);
        securityPresenterImp = new SecurityPresenterImp(this, getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.login_msg7));
        toastor = new Toastor(getActivity());
        securityMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (linkBLE.isLink()) {
                    String msg = MyApplication.newInstance().KEY + "1207" + (isChecked ? "01" : "02") + "000000000000";
                    linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                    SpUtils.putBoolean(SECURITY_SWITCH, isChecked);
                }

            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_security;
    }


    @OnClick({R.id.security_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.security_news:
                startActivity(new Intent(getActivity(), LogActivity.class));
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
    public void loadDataSuccess(Code tData) {

    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        toastor.showSingletonToast(getString(R.string.login_msg10));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(notifyReceiver);
        handler.removeCallbacks(runnable);
    }


    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BluetoothActivity", intent.getAction());
            //设备
            if (EXTRA_STATUS.equals(intent.getAction())) {
                securitySchema.setImageResource(R.drawable.bluetooth_gps_bluetooth);
                //收到设备信息
                DeviceState deviceState = (DeviceState) intent.getSerializableExtra(EXTRA_STATUS);
                getCurBatteryValue((deviceState.getDianchi() * 100));
                securityMessage.setChecked(deviceState.getKaiguan().equals("01"));
                security_name.setText(MyApplication.newInstance().name);
            } else if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                // securityDianliang.setVisibility(View.GONE);
                securitySchema.setImageResource(R.drawable.bluetooth_gps_none);
                securityElectricity.setVisibility(View.GONE);
            }
        }
    };

    /*private void getVoltage(int voltage) {
        securityDianliang.setVisibility(View.VISIBLE);
        securityElectricity.setVisibility(View.VISIBLE);
        Log.e("电量", voltage + "");
        int vol = ((voltage - 32) * 10);
        securityDianliang.setText(vol + "%");
        if (vol <= 10) {
            securityElectricity.setImageResource(R.drawable.battery_0);
        } else if (voltage <= 20) {
            securityElectricity.setImageResource(R.drawable.battery_10);
        } else if (voltage <= 30) {
            securityElectricity.setImageResource(R.drawable.battery_20);
        } else if (voltage <= 60) {
            securityElectricity.setImageResource(R.drawable.battery_40);
        } else if (voltage <= 80) {
            securityElectricity.setImageResource(R.drawable.battery_60);
        } else {
            securityElectricity.setImageResource(R.drawable.battery_90);
        }
    }*/

    public void getCurBatteryValue(int curVoltage) {
        securityElectricity.setVisibility(View.VISIBLE);
        Log.i(TAG, "getCurBatteryValue: " + curVoltage);
        if (curVoltage <= 3500) {
            sendNotification(curVoltage);
            securityElectricity.setImageResource(R.drawable.battery_0);
        } else if (curVoltage <= 3700) {
            securityElectricity.setImageResource(R.drawable.battery_10);
            sendNotification(curVoltage);
        } else if (curVoltage <= 3800) {
            securityElectricity.setImageResource(R.drawable.battery_20);
        } else if (curVoltage <= 3900) {
            securityElectricity.setImageResource(R.drawable.battery_40);
        } else if (curVoltage <= 4000) {
            securityElectricity.setImageResource(R.drawable.battery_60);
        } else {
            securityElectricity.setImageResource(R.drawable.battery_90);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // securityDianliang.setVisibility(View.GONE);
        securitySchema.setImageResource(R.drawable.bluetooth_gps_none);
        securityElectricity.setVisibility(View.GONE);
    }

    private void sendNotification( int id) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(getActivity().getString(R.string.no_title))
                        .setContentText(String.format(getString(R.string.no_msg2),MyApplication.newInstance().name))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilder.build());
    }
}
