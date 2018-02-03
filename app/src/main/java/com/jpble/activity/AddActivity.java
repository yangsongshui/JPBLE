package com.jpble.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import static com.jpble.utils.Constant.ACTION_BLE_KEY_OPERATION_FAILURE;
import static com.jpble.utils.Constant.ACTION_BLE_NOTIFY_DATA;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.SUCCESSFUL_DEVICE_CONNECTION;
import static com.jpble.utils.Constant.closeKeybord;
import static com.jpble.utils.Constant.isSoftInputShow;
import static com.jpble.utils.ToHex.StringToHex2;

public class AddActivity extends BaseActivity implements OnItemCheckListener, View.OnClickListener {

    @BindView(R.id.add_name_et)
    EditText addNameEt;
    @BindView(R.id.add_ble)
    TextView addBle;
    @BindView(R.id.add_rv)
    RecyclerView addRv;
    DeviceAdapter adapter;
    //标记是否在扫描
    private boolean mScanning = false;
    private ProgressDialog pd;
    private LinkBLE linkBLE;
    // 30s 后停止扫描
    private static final long SCAN_PERIOD = 30000;
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
        return R.layout.activity_add;
    }

    @Override
    protected void init() {
        toastor = new Toastor(this);
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
        intentFilter.addAction(ACTION_BLE_KEY_OPERATION_FAILURE);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        intentFilter.addAction(ACTION_BLE_NOTIFY_DATA);
        registerReceiver(notifyReceiver, intentFilter);
        scanLeDevice(true);
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
                    addBle.setText(String.format(getString(R.string.add_msg4), "OFF"));
                }
            }, SCAN_PERIOD);
            //搜索置顶服务BLE设备
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            addBle.setText(String.format(getString(R.string.add_msg4), "ON"));
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            addBle.setText(String.format(getString(R.string.add_msg4), "OFF"));
        }
        invalidateOptionsMenu();
    }

    private void initView() {
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.device_done9));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addRv.setLayoutManager(layoutManager);
        adapter = new DeviceAdapter(this);
        addRv.setAdapter(adapter);
        adapter.setOnItemCheckListener(this);

    }


    @Override
    public void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, BLEDevice bleDevice) {
        mac = bleDevice.getDevice().getAddress();
        Log.i("Bluetooth", "onItemClick: mac=" + mac);
        dialog.show();
        scanLeDevice(false);
    }

    @Override
    public void onClick(View v) {
        String msg = dialog.getMsg();
        if (!msg.equals("")) {
            scanLeDevice(false);
            pd.show();
            String key = "001104" + StringToHex2(msg);
            myApp.deviceKey = key;
            myApp.psw = msg;
            if (linkBLE.isLink()) {
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
            } else {
                linkBLE.LinkBluetooth(mac);
            }
        }
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
                        if (("Track").equals(device.getName())) {
                            if (adapter != null && !devicesMap.containsKey(device.getAddress())) {
                                adapter.setDevice(bleDevice);
                                devicesMap.put(device.getAddress(), bleDevice);
                            }
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
        if (mBluetoothAdapter.isEnabled()) {
            addBle.setText(String.format(getString(R.string.add_msg4), "OFF"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        addNameEt.clearFocus();
        addNameEt.setFocusable(false);
        if (isSoftInputShow(this)) {
            closeKeybord(addNameEt, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyReceiver);
    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("AddActivity", intent.getAction());
            //设备
            if (SUCCESSFUL_DEVICE_CONNECTION.equals(intent.getAction())) {
                toastor.showSingletonToast(getResources().getString(R.string.toastor_msg));
                //获取设备信息
                String key = MyApplication.newInstance().KEY + "FA00";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
                String name = addNameEt.getText().toString().trim();
                if (name.isEmpty())
                    name = "My Geo";
                startActivity(new Intent(AddActivity.this, DeviceActivity.class).putExtra("name", name));
                finish();
            } else if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                toastor.showSingletonToast(getResources().getString(R.string.toastor_msg2));
            }else if (ACTION_BLE_KEY_OPERATION_FAILURE.equals(intent.getAction())){
                toastor.showSingletonToast(getResources().getString(R.string.toastor_msg2));
            }
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    };


    @OnClick({R.id.add_loading, R.id.add_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_loading:
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
                if (!mScanning) {
                    devicesMap.clear();
                    adapter.clearDevice();
                    scanLeDevice(true);
                    Log.e("Click", "搜索");
                }
                //startActivity(new Intent(this, DeviceActivity.class));
                //  finish();
                break;
            case R.id.add_back:
                finish();
                break;
            default:
                break;
        }
    }
}
