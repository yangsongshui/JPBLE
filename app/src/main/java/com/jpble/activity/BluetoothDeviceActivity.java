package com.jpble.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jpble.OnItemCheckListener;
import com.jpble.R;
import com.jpble.adapter.DeviceAdapter;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.ble.BLEDevice;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.widget.CommomDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.SUCCESSFUL_DEVICE_CONNECTION;
import static com.jpble.utils.ToHex.StringToHex2;

public class BluetoothDeviceActivity extends BaseActivity implements OnItemCheckListener, View.OnClickListener {


    @BindView(R.id.bluetooth_name)
    TextView bluetoothName;
    @BindView(R.id.bluetooth_mac)
    TextView bluetoothMac;
    @BindView(R.id.ble_rv)
    RecyclerView bleRv;
    DeviceAdapter adapter;
    //标记是否在扫描
    private boolean mScanning = false;

    private ProgressDialog pd;
    private LinkBLE linkBLE;
    // 10s 后停止扫描
    private static final long SCAN_PERIOD = 10000;
    //保存搜索到的设备
    Map<String, BLEDevice> devicesMap;
    Toastor toastor;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler handler;

    private MyApplication myApp;
    String mac = "";
    private CommomDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_bluetooth_device;
    }

    @Override
    protected void init() {
        myApp = MyApplication.newInstance();
        linkBLE = myApp.getBleManager();
        dialog = new CommomDialog(this, R.style.dialog);
        if (!linkBLE.initBLE(this)) {
            toastor.showSingletonToast(getResources().getString(R.string.ble_abnormal));
        }
        adapter = new DeviceAdapter(this);
        devicesMap = new HashMap<>();//保存搜索到的设备
        initView();
        initBLE();
        handler = new Handler();
        dialog.setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SUCCESSFUL_DEVICE_CONNECTION);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        registerReceiver(notifyReceiver, intentFilter);
    }


    @OnClick({R.id.ble_return, R.id.trip_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ble_return:
                finish();
                break;
            case R.id.trip_share:
                if (!mScanning) {
                    devicesMap.clear();
                    adapter.clearDevice();
                    scanLeDevice(true);
                    Log.e("Click", "搜索");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 扫描设备
     *
     * @param enable 扫描设备标记，true扫描，false停止
     */

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;

                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            //搜索置顶服务BLE设备
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);

        } else {
            mScanning = false;

            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    private void initView() {
        pd = new ProgressDialog(this);
        pd.setMessage("设备连接中");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bleRv.setLayoutManager(layoutManager);
        adapter = new DeviceAdapter(this);
        bleRv.setAdapter(adapter);
        adapter.setOnItemCheckListener(this);
    }

    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device.getName() != null && !device.getAddress().equals(myApp.bindMac)) {
                        final BLEDevice bleDevice = new BLEDevice(device, rssi);
                        //判断是否存在相同的设备

                        if (adapter != null && !devicesMap.containsKey(device.getAddress())) {
                            adapter.setDevice(bleDevice);
                            devicesMap.put(device.getAddress(), bleDevice);
                        }

                    }
                }
            });
        }
    };

    private void initBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            toastor.showSingletonToast(getString(R.string.ble_abnormal));
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            toastor.showSingletonToast(getString(R.string.ble_abnormal));
            mBluetoothAdapter.enable();
            return;
        }

        if (!isLocationEnable(this) && android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.activity_dialog_title_warming));
            builder.setMessage(getResources().getString(R.string.activity_dialog_msg_warming));
            builder.setPositiveButton(getResources().getString(R.string.activity_dialog_connect),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setLocationService();
                        }
                    });
            builder.setNegativeButton(getResources().getString(R.string.activity_dialog_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        }
    }

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;

    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) {
            return true;
        }
        return false;
    }

    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                //定位已打开的处理
            } else {
                //定位依然没有打开的处理
                setLocationService();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, BLEDevice bleDevice) {
        mac = bleDevice.getDevice().getAddress();
        Log.i("Bluetooth", "onItemClick: mac=" + mac);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        String msg = dialog.getMsg();
        if (!msg.equals("")) {
            pd.show();
            String key = "001108" + StringToHex2(msg);
            myApp.deviceKey = key;
            if (linkBLE.isLink()) {
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
            } else {
                linkBLE.LinkBluetooth(mac);
            }
        }
    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BluetoothActivity", intent.getAction());
            //设备
            if (SUCCESSFUL_DEVICE_CONNECTION.equals(intent.getAction())) {
                toastor.showSingletonToast(getResources().getString(R.string.toastor_msg));
                finish();
            } else if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {

                 toastor.showSingletonToast(getResources().getString(R.string.toastor_msg2));
            }
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    };
}
