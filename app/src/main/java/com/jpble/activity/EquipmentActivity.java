package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.widget.EditDialog;
import com.jpble.widget.RadioDialog;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.R.style.dialog;
import static com.jpble.utils.Constant.ACTION_BLE_KEY_OPERATE_SUCCESSFULLY;
import static com.jpble.utils.Constant.ACTION_BLE_KEY_OPERATION_FAILURE;
import static com.jpble.utils.Constant.GPS;
import static com.jpble.utils.Constant.TRACKING_INTERVAL;
import static com.jpble.utils.Constant.VIBRATION_LEVEL;
import static com.jpble.utils.ToHex.StringToHex3;

public class EquipmentActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.equipment_gps)
    SwitchButton equipmentGps;
    @BindView(R.id.equipment_intensity)
    TextView equipmentIntensity;
    @BindView(R.id.equipment_time)
    TextView equipmentTime;
    RadioDialog radioDialog;
    EditDialog editDialog;
    private ProgressDialog pd;
    String gears = "";
    LinkBLE linkBLE;
    Toastor toastor;

    @Override
    protected int getContentView() {
        return R.layout.activity_equipment;
    }

    @Override
    protected void init() {
        toastor = new Toastor(this);
        linkBLE = MyApplication.newInstance().getBleManager();
        pd = new ProgressDialog(this);
        pd.setMessage("操作中...");
        initView();
        radioDialog = new RadioDialog(this, dialog);
        editDialog = new EditDialog(this, dialog);
        editDialog.setOnClickListener(this);
        radioDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (radioDialog.getCheck()) {
                    case R.id.radio_a:
                        gears = "01";
                        equipmentIntensity.setText(getString(R.string.radio_a));
                        break;
                    case R.id.radio_b:
                        gears = "02";
                        equipmentIntensity.setText(getString(R.string.radio_b));
                        break;
                    case R.id.radio_c:
                        gears = "03";
                        equipmentIntensity.setText(getString(R.string.radio_c));
                        break;
                    default:
                        break;
                }
                SpUtils.putString(VIBRATION_LEVEL, gears);
                String key = MyApplication.newInstance().KEY + "1207000000" + gears + "000000";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
                radioDialog.dismiss();
                pd.show();
            }
        });
        equipmentGps.setOnCheckedChangeListener(this);
        initBroadcastReceiver();
    }


    @OnClick({R.id.trip_return, R.id.trip_share, R.id.equipment_intensity_ll, R.id.equipment_time_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.trip_return:
                finish();
                break;
            case R.id.trip_share:
                break;
            case R.id.equipment_time_ll:
                editDialog.show();
                break;
            case R.id.equipment_intensity_ll:
                radioDialog.show();
                radioDialog.init(SpUtils.getString(VIBRATION_LEVEL, "01"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        String msg = editDialog.getMsg();
        if (!msg.equals("")) {
            String key = MyApplication.newInstance().KEY + "12070000000000" + StringToHex3(msg);
            linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
            editDialog.dismiss();
            pd.show();
            SpUtils.putInt(TRACKING_INTERVAL, Integer.parseInt(msg));
            String min = Integer.parseInt(msg) + "min";
            equipmentTime.setText(min);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        String key = MyApplication.newInstance().KEY + "120700000000" + (b ? "01" : "02") + "0000";
        linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
        pd.show();
        SpUtils.putBoolean(GPS, b);

    }

    private void initView() {
        equipmentGps.setCheckedNoEvent(SpUtils.getBoolean(GPS, true));
        String min = SpUtils.getInt(TRACKING_INTERVAL, 1) + "min";
        equipmentTime.setText(min);
        switch (SpUtils.getString(VIBRATION_LEVEL, "01")) {
            case "01":
                equipmentIntensity.setText(getString(R.string.radio_a));
                break;
            case "02":
                equipmentIntensity.setText(getString(R.string.radio_b));
                break;
            case "03":
                equipmentIntensity.setText(getString(R.string.radio_c));
                break;
            default:
                break;

        }
    }

    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BLE_KEY_OPERATION_FAILURE);
        intentFilter.addAction(ACTION_BLE_KEY_OPERATE_SUCCESSFULLY);
        registerReceiver(notifyReceiver, intentFilter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(notifyReceiver);
    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BluetoothActivity", intent.getAction());
            if (ACTION_BLE_KEY_OPERATE_SUCCESSFULLY.equals(intent.getAction())) {
                //  toastor.showSingletonToast("");
            } else if (ACTION_BLE_KEY_OPERATION_FAILURE.equals(intent.getAction())) {

            }

            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    };

}
