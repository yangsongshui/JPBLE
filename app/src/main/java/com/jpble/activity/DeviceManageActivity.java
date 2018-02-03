package com.jpble.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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

import butterknife.OnClick;

import static com.jpble.utils.Constant.BASE_URL;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;

public class DeviceManageActivity extends BaseActivity {

    MyApplication myApp;
    //蓝牙服务类


    LinkBLE linkBLE;
    Toastor toastor;
    Handler handler = new Handler();
    Runnable myRunnable;
    DeletePresenterImp deletePresenterImp;
    DeleteSimPresenterImp deleteSimPresenterImp;
    ProgressDialog progressDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_device_manage;
    }

    @Override
    protected void init() {
        myApp = MyApplication.newInstance();
        linkBLE = myApp.getBleManager();
        initBroadCast();
        toastor = new Toastor(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        myRunnable = new Runnable() {
            @Override
            public void run() {
                MyApplication.newInstance().bindMac = "";
                linkBLE.closeBle();

            }
        };

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
                if (tData.getCode() != 200)
                    err(tData.getCode());
                finish();
            }

            @Override
            public void loadDataError(Throwable throwable) {
                showToastor(getString(R.string.login_msg10));
            }
        }, this);
    }


    @OnClick({R.id.ble_return, R.id.btn_close, R.id.btn_cancel, R.id.btn_cancel_close, R.id.btn_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ble_return:
                finish();
                break;
            case R.id.btn_close:
                sureShutDownDialog();
                break;
            case R.id.btn_cancel:
                sureCancelMatchingDialog();
                break;
            case R.id.btn_cancel_close:
                sureCancelAndCloseDialog();
                break;
            case R.id.btn_update:
                // mBLEService.writeDeviceUpdate();
                break;
            default:
                break;
        }
    }

    /*关闭设备弹窗提示*/
    private void sureShutDownDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activity_device_dialog_title_warming);
        builder.setMessage(R.string.close_device_tip);
        builder.setPositiveButton(R.string.activity_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shutDownDevice();
            }
        });
        builder.setNegativeButton(R.string.activity_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void shutDownDevice() {
        // if (linkBLE.write(Constant.getData(Constant.COMMAND_INFO1, "05", "01000000"))) {
        MyApplication.newInstance().bindMac = "";
        linkBLE.closeBle();


    }


    /*初始化监听广播*/
    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        registerReceiver(notifyReceiver, intentFilter);
    }

    /*提示重新连接设备*/
    private void showUnConnectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activity_device_dialog_title_warming);
        builder.setMessage(R.string.error_un_connect_tip);
        builder.setPositiveButton(R.string.activity_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();

    }

    /*关闭设备蓝牙*/
    private void sureCancelAndCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activity_device_dialog_title_warming);
        builder.setMessage(R.string.close_and_unpairing_device_tip);
        builder.setPositiveButton(R.string.activity_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = MyApplication.newInstance().KEY + "13020001";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                handler.postDelayed(myRunnable, 2000);
            }
        });
        builder.setNegativeButton(R.string.activity_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /*取消配对*/
    private void sureCancelMatchingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activity_device_dialog_title_warming);
        builder.setMessage(R.string.cancel_pairing_device_tip);
        builder.setPositiveButton(R.string.activity_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = MyApplication.newInstance().KEY + "13020100";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                delete();
            }
        });
        builder.setNegativeButton(R.string.activity_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("收到设备信息广播", intent.getAction());
            //设备
            if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                showUnConnectDialog();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyReceiver);
    }

    private void delete() {
        String id = MyApplication.newInstance().id;

        if (!id.isEmpty()) {
            id = id.substring(0, id.length() - 1);
            Log.e("id", id);
            String token = MyApplication.newInstance().getUser().getData().getToken();
            String url = BASE_URL + "user/lock/10003/" + id;
            deletePresenterImp.register(url, id, token);
        }

    }

    private void deleteSim() {
        String id = MyApplication.newInstance().carId;
        Log.e("id", id);
        String token = MyApplication.newInstance().getUser().getData().getToken();
        String url = BASE_URL + "user/simcard/10005/" + id;
        deleteSimPresenterImp.register(url, id, token);
    }

}
