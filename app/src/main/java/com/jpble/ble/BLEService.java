package com.jpble.ble;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jpble.app.MyApplication;
import com.jpble.utils.Toastor;

import java.util.Date;

import static com.jpble.utils.Constant.ACTION_BLE_NOTIFY_DATA;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.SUCCESSFUL_DEVICE_CONNECTION;


/**
 * Created by ys on 2017/7/17.
 */

public class BLEService extends Service {
    private static final long HEART_PERIOD = 2500;
    Toastor toastor;
    LinkBLE linkBLE;
    Handler handler = new Handler();
    Runnable runnable;
    Runnable runnable2;
    boolean mScanning = false;
    Date date;
    /**
     * 搜索BLE终端
     */
    private BluetoothAdapter mBluetoothAdapter;

    //服务初始化
    @Override
    public void onCreate() {
        initBroadCast();
        super.onCreate();
        date = new Date();
        linkBLE = MyApplication.newInstance().getBleManager();
        toastor = new Toastor(this);
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Log.i("BLEService", "服务启动了");
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("BLEService", "心跳包");

                handler.postDelayed(this, HEART_PERIOD);
            }

        };
        runnable2 = new Runnable() {
            @Override
            public void run() {

                scanLeDevice(true);
            }

        };

    }

    /*初始化监听广播*/
    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SUCCESSFUL_DEVICE_CONNECTION);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        intentFilter.addAction(ACTION_BLE_NOTIFY_DATA);
        registerReceiver(notifyReceiver, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyReceiver);
    }


    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("BLEService收到设备信息广播", intent.getAction());
            //设备
            if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                handler.removeCallbacks(runnable);
                if (MyApplication.newInstance().bindMac != null) {
                    mScanning = false;
                    handler.postDelayed(runnable2, 5000);
                }
            } else if (SUCCESSFUL_DEVICE_CONNECTION.equals(intent.getAction())) {
                handler.postDelayed(runnable, 1000);

            }

        }

    };


    /**
     * 扫描设备
     *
     * @param enable 扫描设备标记，true扫描，false停止
     */

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            //搜索置顶服务BLE设备
            Log.e("BLEService", "开始搜索2");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            if (device.getName() != null && (device.getName().toLowerCase().contains("Meter".toLowerCase())
                    || device.getName().toLowerCase().equals("DIALOG-SPS".toLowerCase()))) {
                Log.e("BLEService", device.getName());
                if (device.getAddress().equals(MyApplication.newInstance().bindMac) && !mScanning) {
                    toastor.showSingletonToast("发现设备,重新连接中...");
                    linkBLE.LinkBluetooth(device.getAddress());
                    scanLeDevice(false);
                }
            }
        }
    };


}

