package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Code;
import com.jpble.ble.LinkBLE;
import com.jpble.presenter.DeletePresenterImp;
import com.jpble.presenter.DeleteSimPresenterImp;
import com.jpble.utils.Constant;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.view.CodeView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.BASE_URL;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.SUCCESSFUL_DEVICE_CONNECTION;
import static com.jpble.utils.ToHex.StringToHex2;

public class AssetInfoActivity extends BaseActivity {


    @BindView(R.id.info_name)
    TextView infoName;
    @BindView(R.id.info_iccid)
    TextView infoIccid;
    @BindView(R.id.info_id)
    TextView infoId;
    @BindView(R.id.info_psw)
    TextView infoPsw;
    @BindView(R.id.info_type)
    TextView infoType;
    @BindView(R.id.info_mac)
    TextView infoMac;
    @BindView(R.id.info_apn)
    TextView info_apn;
    String msg = "";
    String id = "";
    String lockId = "";
    String psw = "";
    DeletePresenterImp deletePresenterImp;
    DeleteSimPresenterImp deleteSimPresenterImp;
    ProgressDialog progressDialog;
    LinkBLE linkBLE;
    private ProgressDialog pd;
    Toastor toastor;

    @Override
    protected int getContentView() {
        return R.layout.activity_asset_info;
    }

    @Override
    protected void init() {
        toastor = new Toastor(this);
        linkBLE = MyApplication.newInstance().getBleManager();
        infoName.setText(getIntent().getStringExtra("name"));
        infoMac.setText(getIntent().getStringExtra("mac"));
        msg = getIntent().getStringExtra("msg");
        id = getIntent().getStringExtra("id");
        psw = getIntent().getStringExtra("psw");
        lockId = getIntent().getStringExtra("lockId");
        pd = new ProgressDialog(this);
        pd.setMessage("设备连接中");
        infoIccid.setText(msg.substring(msg.indexOf("ICCID:") + 6, msg.length() - 1));
        info_apn.setText(msg.substring(msg.indexOf("APN:") + 4, msg.indexOf(",USER")));
        infoPsw.setText(msg.substring(msg.indexOf("USER_PWD:") + 9, msg.indexOf(",ICCID")));
        infoId.setText(msg.substring(msg.indexOf("USER:") + 5, msg.indexOf(",USER_PWD")));
        infoType.setText(getIntent().getStringExtra("type"));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SUCCESSFUL_DEVICE_CONNECTION);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        registerReceiver(notifyReceiver, intentFilter);
        deletePresenterImp = new DeletePresenterImp(new CodeView() {

            @Override
            public void showProgress() {
                progressDialog.show();
            }

            @Override
            public void disimissProgress() {

            }


            @Override
            public void loadDataSuccess(Code tData) {
                if (tData.getCode() == 200)
                    deleteSim();
                else
                    err(tData.getCode());
            }

            @Override
            public void loadDataError(Throwable throwable) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.e("loadDataError", throwable.getMessage());
                showToastor(getString(R.string.login_msg10));
            }
        }, this);
        deleteSimPresenterImp = new DeleteSimPresenterImp(new CodeView() {
            @Override
            public void showProgress() {

            }

            @Override
            public void disimissProgress() {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void loadDataSuccess(Code tData) {
                if (tData.getCode() == 200)
                    finish();
                else
                    err(tData.getCode());
            }

            @Override
            public void loadDataError(Throwable throwable) {
                showToastor(getString(R.string.login_msg10));
            }
        }, this);
    }

    @OnClick({R.id.info_edit, R.id.info_back, R.id.info_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.info_edit:
                if (MyApplication.newInstance().bindMac != null && MyApplication.newInstance().bindMac.equals(infoMac.getText().toString())) {
                    //获取设备信息
                    String key = MyApplication.newInstance().KEY + "FA00";
                    linkBLE.write(Constant.jiami("FE", ToHex.random(), key));

                    startActivity(new Intent(this, DeviceActivity.class).putExtra("name", infoName.getText().toString()).putExtra("type", 0)
                            .putExtra("apn", infoType.getText().toString()).putExtra("lockId", lockId).putExtra("id", id));
                    finish();
                } else {
                    linkBLE.closeBle();
                    pd.show();
                    String key = "001104" + StringToHex2(psw);
                    MyApplication.newInstance().deviceKey = key;
                    MyApplication.newInstance().psw = psw;
                    linkBLE.LinkBluetooth(infoMac.getText().toString());
                }
                break;
            case R.id.info_back:
                finish();
                break;
            case R.id.info_delete:
                delete();
                break;
        }
    }

    private void delete() {

        String token = MyApplication.newInstance().getUser().getData().getToken();
        String url = BASE_URL + "user/lock/10003/" + lockId;
        deletePresenterImp.register(url, lockId, token);
    }

    private void deleteSim() {
        String token = MyApplication.newInstance().getUser().getData().getToken();
        String url = BASE_URL + "user/simcard/10005/" + id;
        deleteSimPresenterImp.register(url, id, token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyReceiver);
        deletePresenterImp.unSubscribe();
        deleteSimPresenterImp.unSubscribe();
    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BluetoothActivity", intent.getAction());
            //设备
            if (SUCCESSFUL_DEVICE_CONNECTION.equals(intent.getAction())) {
                //获取设备信息
                String key = MyApplication.newInstance().KEY + "FA00";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
                startActivity(new Intent(AssetInfoActivity.this, DeviceActivity.class).putExtra("name", infoName.getText().toString()).putExtra("type", 0)
                        .putExtra("apn", infoType.getText().toString()).putExtra("lockId", lockId).putExtra("id", id));
                finish();
            } else if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
               // toastor.showSingletonToast(getResources().getString(R.string.toastor_msg2));
            }
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    };

}
