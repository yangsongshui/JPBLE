package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Code;
import com.jpble.ble.LinkBLE;
import com.jpble.presenter.BindingPresenterImp;
import com.jpble.utils.Constant;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.view.CodeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.ble.CRCUtil.returnCRC2;
import static com.jpble.utils.Constant.ACTION_BLE_DEVICE_LINK_WEB;
import static com.jpble.utils.Constant.ACTION_BLE_DEVICE_UNLINK_WEB;
import static com.jpble.utils.Constant.ACTION_BLE_NOTIFY_DATA;
import static com.jpble.utils.Constant.ACTION_DATA;
import static com.jpble.utils.Constant.BASE_URL;
import static com.jpble.utils.Constant.DEVICE_INFO;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.jiami;

public class DeviceActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, CodeView {

    @BindView(R.id.device_imsi)
    TextView deviceImsi;
    @BindView(R.id.device_apn)
    EditText deviceApn;
    @BindView(R.id.device_id)
    EditText deviceId;
    @BindView(R.id.device_psw)
    EditText devicePsw;
    @BindView(R.id.device_rg)
    RadioGroup deviceRg;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.device_test)
    ImageView deviceTest;
    @BindView(R.id.device_done2)
    ImageView deviceDone2;
    @BindView(R.id.device_done)
    TextView deviceDone;
    LinkBLE linkBLE;
    Toastor toastor;
    private ProgressDialog pd;
    BindingPresenterImp bindingPresenterImp;
    String name;

    @Override
    protected int getContentView() {
        return R.layout.activity_device;
    }

    @Override
    protected void init() {
        toastor = new Toastor(this);
        pd = new ProgressDialog(this);
        pd.setMessage("数据获取中...");
        name = getIntent().getStringExtra("name");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        intentFilter.addAction(ACTION_BLE_NOTIFY_DATA);
        intentFilter.addAction(ACTION_DATA);
        intentFilter.addAction(ACTION_BLE_DEVICE_LINK_WEB);
        intentFilter.addAction(ACTION_BLE_DEVICE_UNLINK_WEB);
        registerReceiver(notifyReceiver, intentFilter);
        linkBLE = MyApplication.newInstance().getBleManager();
        pd.show();
        deviceRg.check(R.id.device_none);
        deviceRg.setOnCheckedChangeListener(this);
        bindingPresenterImp = new BindingPresenterImp(this, this);

    }


    @OnClick({R.id.device_done, R.id.device_back, R.id.device_test, R.id.device_done2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.device_done:
                Map<String, String> map = new HashMap<>();
                String url = BASE_URL + "user/lock/10002/" + MyApplication.newInstance().bindMac;
                map.put("mac", MyApplication.newInstance().bindMac);
                map.put("name", name);
                map.put("token", MyApplication.newInstance().getUser().getData().getToken());
                bindingPresenterImp.register(url, map);
                break;
            case R.id.device_back:
                finish();
                break;
            case R.id.device_test:
                //测试网络
                pd.show();
                String s = MyApplication.newInstance().KEY + "140001";
                linkBLE.write(jiami("FE", ToHex.random(), s));
                break;
            case R.id.device_done2:
                //写入数据
                pd.show();
                msg = msg.replace(iccid, deviceImsi.getText().toString());
                msg = msg.replace(apn, deviceApn.getText().toString());
                msg = msg.replace(user, deviceId.getText().toString());
                msg = msg.replace(pin, devicePsw.getText().toString());
                MyApplication.newInstance().msg = msg;
                Log.e("msg", msg);
                getByte(msg);
                break;
            default:
                break;
        }
    }

    String msg = "";
    String iccid = "";
    String apn = "";
    String user = "";
    String pin = "";
    String type = "";
    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BluetoothActivity", intent.getAction());
            //设备
            if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                toastor.showSingletonToast(getResources().getString(R.string.toastor_msg2));
            } else if (ACTION_BLE_NOTIFY_DATA.equals(intent.getAction())) {
                msg = intent.getStringExtra(DEVICE_INFO);
                type = intent.getStringExtra("type");
                iccid = msg.substring(msg.indexOf("ICCID:") + 6, msg.indexOf(",VERSIO"));
                apn = msg.substring(msg.indexOf("APN:") + 4, msg.indexOf(",USER"));
                user = msg.substring(msg.indexOf("USER:") + 5, msg.indexOf(",PIN"));
                pin = msg.substring(msg.indexOf("PIN:") + 4, msg.indexOf(",IPMODE"));
                deviceImsi.setText(iccid);
                deviceId.setText(user);
                devicePsw.setText(pin);
                deviceApn.setText(apn);
            } else if (ACTION_BLE_DEVICE_LINK_WEB.equals(intent.getAction())) {
                //设备联网成功
                showToastor(getString(R.string.device_done7));

                deviceTest.setVisibility(View.GONE);
                deviceDone.setVisibility(View.VISIBLE);
            } else if (ACTION_BLE_DEVICE_UNLINK_WEB.equals(intent.getAction())) {
                //设备联网失败
                showToastor(getString(R.string.device_done6));
                deviceTest.setImageResource(R.drawable.ng);
            } else if (ACTION_DATA.equals(intent.getAction())) {
                deviceTest.setVisibility(View.VISIBLE);
                deviceDone2.setVisibility(View.GONE);
                showToastor(getString(R.string.add_bt));
            }
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyReceiver);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.device_none:
                //apn模式
                break;
            case R.id.device_pap:
                //用户账号密码模式
                break;
        }
    }

    Map<String, byte[]> data = new HashMap<>();

    private void getByte(String s) {
        data.clear();
        byte[] bytes = s.getBytes();
        int len = s.length() / 16;
        int len2 = s.length() % 16;
        int indext = 0;
        if (len2 != 0) {
            len = len + 1;
        }
        for (int i = 0; i < len; i++) {
            byte[] msg = new byte[16];
            for (int j = 0; j < msg.length; j++, indext++) {
                if (indext >= bytes.length)
                    msg[j] = 0;
                else
                    msg[j] = bytes[indext];
            }
            Log.e("getByte", len + " " + new String(msg));
            data.put(String.valueOf(i), msg);
        }
        linkBLE.setHashMap(data);
        String key = MyApplication.newInstance().KEY + "FC05" + ToHex.StringToHex3(len + "") + ToHex.bytesToHex(returnCRC2(bytes)) + type;
        linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
    }

    @Override
    public void showProgress() {
        pd.show();
    }

    @Override
    public void disimissProgress() {
        if (pd.isShowing())
            pd.dismiss();
    }

    @Override
    public void loadDataSuccess(Code tData) {
        Log.e("Code", tData.toString());
        if (tData.getCode() == 200) {
            startActivity(new Intent(DeviceActivity.this, HomeActivity.class));
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

}
