package com.jpble.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.jpble.app.MyApplication;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;

import static com.jpble.utils.Constant.ACTION_BLE_KEY_OPERATE_SUCCESSFULLY;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.GPS;
import static com.jpble.utils.Constant.LOCK_STATUS;
import static com.jpble.utils.Constant.SECURITY_SWITCH;
import static com.jpble.utils.Constant.SUCCESSFUL_DEVICE_CONNECTION;
import static com.jpble.utils.Constant.TRACKING_INTERVAL;
import static com.jpble.utils.Constant.UUID_CHARACTERISTIC_CONTROL;
import static com.jpble.utils.Constant.UUID_CHARACTERISTIC_NOTIFY_DATA;
import static com.jpble.utils.Constant.UUID_SERVICE;
import static com.jpble.utils.Constant.VIBRATION_LEVEL;
import static com.jpble.utils.Constant.VIBRATION_SWITCH;
import static com.jpble.utils.SpUtils.getString;


/**
 * Created by Administrator on 2016/7/18.
 */
public class LinkBLE implements BluetoothLeClass.OnDisconnectListener {
    private final static String TAG = LinkBLE.class.getSimpleName();
    private BluetoothLeClass mBLE;
    private BluetoothGattCharacteristic notifiChara;
    // private BluetoothGattCharacteristic readChara;
    private BluetoothGattCharacteristic writeChara;
    private boolean isRing = true;
    private boolean isLink = false;
    private String address;
    private Runnable runnable2;
    private Runnable runnable;

    public String getAddress() {
        return address;
    }

    private BluetoothAdapter mBluetoothAdapter;
    Context context;
    String str;
    Handler handler = new Handler();

    public boolean isLink() {
        return isLink;
    }

    public void setIsLink(boolean isLink) {
        this.isLink = isLink;
    }

    public LinkBLE(final Context context) {
        this.context = context;
        mBLE = new BluetoothLeClass(context);
        if (!mBLE.initialize()) {
            MyApplication.newInstance().clearAllActies();
        }
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        mBLE.setOnDisconnectListener(this);
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        runnable2 = new Runnable() {
            @Override
            public void run() {
                //  write(Constant.COMMAND_KEY + MyApplication.newInstance().connectKey);
                handler.postDelayed(runnable2, 1000);
                write(Constant.jiami("FE", ToHex.random(), "001108794f546d4b35307a"));
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                String gps = SpUtils.getBoolean(GPS, true) ? "01" : "02";
                String interval = ToHex.StringToHex3(String.valueOf(SpUtils.getInt(TRACKING_INTERVAL, 1)));
                String status = getString(LOCK_STATUS, "01");
                String leve = SpUtils.getString(VIBRATION_LEVEL, "01");
                String security = SpUtils.getBoolean(SECURITY_SWITCH, false) ? "01" : "02";
                String vibration = SpUtils.getBoolean(VIBRATION_SWITCH, true) ? "01" : "02";
                String msg = MyApplication.newInstance().KEY + "1208" + security + status + vibration + leve + gps + interval;
                write(Constant.jiami("FE", ToHex.random(), msg));
            }
        };
    }

    /**
     * 连接设备
     */
    public boolean LinkBluetooth(String address) {
        this.address = address.toUpperCase();
        Log.e(TAG, address + "连接中...");
        return mBLE.connect(this.address);
    }

    /**
     * 搜索到BLE终端服务的事件
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            Log.i(TAG, "发现服务" + gatt.getDevice().getName());
            BluetoothGattService bleGattService = gatt.getService(UUID_SERVICE);
            writeChara = bleGattService.getCharacteristic(UUID_CHARACTERISTIC_CONTROL);
            notifiChara = bleGattService.getCharacteristic(UUID_CHARACTERISTIC_NOTIFY_DATA);
            //订阅通知
            mBLE.setCharacteristicNotification(notifiChara, true);
            handler.postDelayed(runnable2, 500);
            MyApplication.newInstance().isBind = true;
            setIsLink(true);
            Intent intent = new Intent();
            intent.setAction(SUCCESSFUL_DEVICE_CONNECTION);
            context.sendBroadcast(intent);
            isRing = false;
            handler.removeCallbacks(runnable2);
        }

    };

    /**
     * 收到BLE终端数据交互的事件
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {

        /**
         * BLE终端数据被读的事件
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharRead " + gatt.getDevice().getName()
                        + " read "
                        + characteristic.getUuid().toString()
                        + " -> "
                        + ToHex.bytesToHex(characteristic.getValue()));
            }

        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName()
                    + " write "
                    + characteristic.getUuid().toString()
                    + " -> "
                    + ToHex.bytesToHex(characteristic.getValue()));

            String msg = ToHex.bytesToHex(characteristic.getValue());
            getData(characteristic.getValue());

        }

    };

    //写入数据
    public boolean write(String string) {
        Log.e("write", string);
        if (writeChara != null) {
            writeChara.setValue(ToHex.hexStringToBytes(string));
            return mBLE.writeCharacteristic(writeChara);
        }
        return false;
    }

    //读取数据
    public void read() {
        /*if (readChara != null)
            mBLE.readCharacteristic(readChara);*/
    }

    @Override
    public void onDisconnect(BluetoothGatt gatt) {
        MyApplication.newInstance().isBind = false;
        mBluetoothAdapter.enable();
        writeChara = null;
        closeBle();
        Log.e(TAG, "设备已断开");
        if (mBluetoothAdapter.isEnabled()) {
            LinkBluetooth(address);
            Log.e(TAG, "重新连接中...");
        }

    }

    public boolean initBLE(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            return false;
        }
        mBluetoothAdapter.enable();
        return true;
    }

    public void closeBle() {

        setIsLink(false);
        isRing = true;
        mBLE.close();
        mBLE.disconnect();
        handler.removeCallbacks(runnable2);
        MyApplication.newInstance().isBind = false;

        writeChara = null;
        Intent intent = new Intent();
        intent.setAction(EQUIPMENT_DISCONNECTED);
        context.sendBroadcast(intent);
    }

    private void getData(byte[] data) {
        byte[] bytes = Constant.jiemi(data);
        Intent intent = new Intent();
        switch (bytes[1]) {
            case 11:
                //通讯权限获取
                intent.setAction(SUCCESSFUL_DEVICE_CONNECTION);
                context.sendBroadcast(intent);
                MyApplication.newInstance().KEY = ToHex.byteToHex(bytes[3]);
                handler.postDelayed(runnable, 500);
                break;
            case 12:
                intent.setAction(ACTION_BLE_KEY_OPERATE_SUCCESSFULLY);
                context.sendBroadcast(intent);
                break;
            case 13:
                break;
            case 31:
                ToHex.byteToHex(data[3]);//电量
                ToHex.byteToHex(data[4]);//防盗开关
                ToHex.byteToHex(data[5]);//锁车状态
                ToHex.byteToHex(data[6]);//震动开关
                ToHex.byteToHex(data[7]);//震动等级
                ToHex.byteToHex(data[8]);//gps
                ToHex.bytesToHex(new byte[]{data[9], data[10]});//定位间隔
                ToHex.byteToHex(data[11]);//定位间隔
                ToHex.byteToHex(data[12]);//主版本号
                ToHex.byteToHex(data[13]);//次版本号
                ToHex.byteToHex(data[14]);//版本修改次数
                break;
            case 21:
                ToHex.byteToHex(data[3]);//开锁状态
                break;
            default:
                break;
        }
    }
}
