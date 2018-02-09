package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.AddCode;
import com.jpble.bean.AddSim;
import com.jpble.bean.Code;
import com.jpble.ble.LinkBLE;
import com.jpble.presenter.AddSimgPresenterImp;
import com.jpble.presenter.BindingPresenterImp;
import com.jpble.presenter.UpSimgPresenterImp;
import com.jpble.utils.AESUtil;
import com.jpble.utils.Constant;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.view.AddCodeView;
import com.jpble.view.AddSimView;
import com.jpble.view.CodeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.ble.CRCUtil.returnCRC2;
import static com.jpble.utils.AESUtil.PHONE_KEY;
import static com.jpble.utils.Constant.ACTION_BLE_DEVICE_LINK_WEB;
import static com.jpble.utils.Constant.ACTION_BLE_DEVICE_UNLINK_WEB;
import static com.jpble.utils.Constant.ACTION_BLE_NOTIFY_DATA;
import static com.jpble.utils.Constant.ACTION_DATA;
import static com.jpble.utils.Constant.BASE_URL;
import static com.jpble.utils.Constant.DEVICE_INFO;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.jiami;

public class DeviceActivity extends BaseActivity implements AddCodeView {

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
    private ProgressDialog pd2;
    BindingPresenterImp bindingPresenterImp;
    AddSimgPresenterImp addSimgPresenterImp;
    UpSimgPresenterImp upSimgPresenterImp;
    String name = "";
    String msg = "";
    String iccid = "";
    String apn = "";
    String user = "";
    String pin = "";
    String type = "";
    String id = "";
    int web = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_device;
    }

    @Override
    protected void init() {
        toastor = new Toastor(this);
        pd = new ProgressDialog(this);
        pd2 = new ProgressDialog(this);
        pd.setMessage(getString(R.string.device_done8));
        pd2.setMessage(getString(R.string.device_done10));
        name = getIntent().getStringExtra("name");
        apn = getIntent().getStringExtra("apn");
        id = getIntent().getStringExtra("id");
        lockId = getIntent().getStringExtra("lockId");
        web = getIntent().getIntExtra("type", 1);

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
        bindingPresenterImp = new BindingPresenterImp(this, this);
        addSimgPresenterImp = new AddSimgPresenterImp(new AddSimView() {
            @Override
            public void showProgress() {

            }

            @Override
            public void disimissProgress() {
                if (pd.isShowing())
                    pd.dismiss();
            }


            @Override
            public void loadDataSuccess(AddSim tData) {
                Log.e("Code", tData.toString());
                if (tData.getCode() == 200) {
                    MyApplication.newInstance().id = lockId;
                    MyApplication.newInstance().carId = String.valueOf(tData.getData().getCardId());
                    startActivity(new Intent(DeviceActivity.this, AssetInfoActivity.class)
                            .putExtra("lockId", lockId)
                            .putExtra("id", String.valueOf(tData.getData().getCardId())).putExtra("type", deviceRg.getCheckedRadioButtonId() == R.id.device_none ? "APN" : "ALL")
                            .putExtra("name", name).putExtra("mac", MyApplication.newInstance().bindMac).putExtra("msg", msg));
                    finish();
                }else {
                    err(tData.getCode());
                }
            }

            @Override
            public void loadDataError(Throwable throwable) {

            }
        }, this);
        upSimgPresenterImp = new UpSimgPresenterImp(new CodeView() {
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
                if (tData.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void loadDataError(Throwable throwable) {

            }
        }, this);
    }


    @OnClick({R.id.device_done, R.id.device_back, R.id.device_test, R.id.device_done2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.device_done:
                if (web == 1) {
                    Map<String, String> map = new HashMap<>();
                    String url = BASE_URL + "user/lock/10002/" + MyApplication.newInstance().bindMac;
                    map.put("mac", MyApplication.newInstance().bindMac);
                    map.put("name", name);
                    map.put("password", AESUtil.aesEncrypt(MyApplication.newInstance().psw, PHONE_KEY));
                    map.put("token", MyApplication.newInstance().getUser().getData().getToken());
                    bindingPresenterImp.register(url, map);
                } else {
                    String url = BASE_URL + "user/simcard/10003/" + id;
                    Map<String, String> map = new HashMap();
                    map.put("number", deviceImsi.getText().toString());
                    map.put("apn", deviceApn.getText().toString());
                    map.put("password", AESUtil.aesEncrypt(devicePsw.getText().toString(), PHONE_KEY));
                    map.put("token", MyApplication.newInstance().getUser().getData().getToken());
                    map.put("authType", deviceRg.getCheckedRadioButtonId() == R.id.device_none ? "APN" : "ALL");
                    map.put("lockId", lockId);
                    map.put("cardId", id);
                    map.put("account", deviceId.getText().toString());
                    upSimgPresenterImp.register(url, map);
                }

                break;
            case R.id.device_back:
                finish();
                break;
            case R.id.device_test:
                //测试网络
                pd2.show();
                String s = "";
                if (deviceRg.getCheckedRadioButtonId() == R.id.device_none)
                    s = MyApplication.newInstance().KEY + "140101";
                else
                    s = MyApplication.newInstance().KEY + "140102";
                linkBLE.write(jiami("FE", ToHex.random(), s));
                break;
            case R.id.device_done2:
                //写入数据
                pd.show();
                msg = msg.replace(iccid, "898602B1191650539966");
                msg = msg.replace(apn, deviceApn.getText().toString());
                msg = msg.replace("USER:" + user, "USER:" + deviceId.getText().toString());
                msg = msg.replace("USER_PWD:" + pin, "USER_PWD:" + devicePsw.getText().toString());
                MyApplication.newInstance().msg = msg;
                Log.e("msg", msg);
                getByte(msg);
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
            if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                toastor.showSingletonToast(getResources().getString(R.string.toastor_msg2));
                finish();
            } else if (ACTION_BLE_NOTIFY_DATA.equals(intent.getAction())) {
                msg = intent.getStringExtra(DEVICE_INFO);
                type = intent.getStringExtra("type");
                iccid = msg.substring(msg.indexOf("ICCID:") + 6, msg.length() - 1);
                apn = msg.substring(msg.indexOf("APN:") + 4, msg.indexOf(",USER"));
                user = msg.substring(msg.indexOf("USER:") + 5, msg.indexOf(",USER_PWD"));
                pin = msg.substring(msg.indexOf("USER_PWD:") + 9, msg.indexOf(",ICCID"));
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
                deviceTest.setVisibility(View.GONE);
                deviceDone.setVisibility(View.VISIBLE);
                deviceTest.setImageResource(R.drawable.ng);
            } else if (ACTION_DATA.equals(intent.getAction())) {
                if (data.size() > 0) {
                    deviceTest.setVisibility(View.VISIBLE);
                    deviceDone.setVisibility(View.GONE);
                    deviceDone2.setVisibility(View.GONE);
                    showToastor(getString(R.string.add_bt));
                    data.clear();
                }

            }
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (pd2.isShowing())
                pd2.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addSimgPresenterImp.unSubscribe();
        bindingPresenterImp.unSubscribe();
        unregisterReceiver(notifyReceiver);
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
        String crc = "";
        for (int i = 0; i < len; i++) {
            byte[] msg = new byte[16];
            for (int j = 0; j < msg.length; j++, indext++) {
                if (indext >= bytes.length)
                    msg[j] = 0;
                else
                    msg[j] = bytes[indext];
            }
            Log.e("getByte", len + " " + new String(msg));
            crc = crc + ToHex.bytesToHex(msg);
            data.put(String.valueOf(i), msg);
        }
        linkBLE.setHashMap(data);


        //"24F4"
        String key = MyApplication.newInstance().KEY + "FC05" + ToHex.StringToHex3(len + "") + ToHex.bytesToHex(returnCRC2(ToHex.hexStringToBytes(crc))) + type;
        linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
    }

    @Override
    public void showProgress() {
        pd.show();
    }

    @Override
    public void disimissProgress() {

    }

    String lockId = "";

    @Override
    public void loadDataSuccess(AddCode tData) {
        Log.e("Code", tData.toString());

        if (tData.getCode() == 200) {
            Map<String, String> map = new HashMap();
            map.put("number", deviceImsi.getText().toString());
            map.put("apn", deviceApn.getText().toString());
            map.put("password", AESUtil.aesEncrypt(devicePsw.getText().toString(), PHONE_KEY));
            map.put("token", MyApplication.newInstance().getUser().getData().getToken());
            map.put("authType", deviceRg.getCheckedRadioButtonId() == R.id.device_none ? "APN" : "ALL");
            lockId = String.valueOf(tData.getData().getLockId());
            map.put("lockId", lockId);
            map.put("account", deviceId.getText().toString());
            addSimgPresenterImp.register(map);
        }else {
            pd.dismiss();
            err(tData.getCode());
        }
    }


    @Override
    public void loadDataError(Throwable throwable) {
        pd.dismiss();
     //   Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

}
