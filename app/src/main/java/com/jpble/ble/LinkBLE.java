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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jpble.ble.CRCUtil.CheckCRC;
import static com.jpble.utils.Constant.ACTION_BLE_DEVICE_LINK_WEB;
import static com.jpble.utils.Constant.ACTION_BLE_DEVICE_UNLINK_WEB;
import static com.jpble.utils.Constant.ACTION_BLE_KEY_OPERATE_SUCCESSFULLY;
import static com.jpble.utils.Constant.ACTION_BLE_NOTIFY_DATA;
import static com.jpble.utils.Constant.DEVICE_INFO;
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
import static com.jpble.utils.ToHex.byteToHex;
import static com.jpble.utils.ToHex.bytesToHex;
import static com.jpble.utils.ToHex.hexStringToBytes;


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
    int size = 0; //数据包长度
    Map<String, byte[]> hashMap = new HashMap<>();

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
                // write(Constant.COMMAND_KEY + MyApplication.newInstance().connectKey);
                // handler.postDelayed(runnable2, 1000);
                write(Constant.jiami("FE", ToHex.random(), MyApplication.newInstance().deviceKey));
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                String gps = SpUtils.getBoolean(GPS, true) ? "01" : "02";
                String interval = ToHex.StringToHex3(String.valueOf(SpUtils.getInt(TRACKING_INTERVAL, 1)));
                String status = SpUtils.getString(LOCK_STATUS, "01");
                String leve = SpUtils.getString(VIBRATION_LEVEL, "01");
                String security = SpUtils.getBoolean(SECURITY_SWITCH, false) ? "01" : "02";
                String vibration = SpUtils.getBoolean(VIBRATION_SWITCH, true) ? "01" : "02";
                String msg = MyApplication.newInstance().KEY + "1207" + security + status + vibration + leve + gps + interval;
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
            //getService(gatt);
            BluetoothGattService bleGattService = gatt.getService(UUID_SERVICE);
            writeChara = bleGattService.getCharacteristic(UUID_CHARACTERISTIC_CONTROL);
            notifiChara = bleGattService.getCharacteristic(UUID_CHARACTERISTIC_NOTIFY_DATA);
            //订阅通知
            mBLE.setCharacteristicNotification(notifiChara, true);
            handler.postDelayed(runnable2, 500);
            MyApplication.newInstance().isBind = true;
            setIsLink(true);
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
                        + bytesToHex(characteristic.getValue()));
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
                    + bytesToHex(characteristic.getValue()));


            getData(characteristic.getValue());

        }

    };

    //写入数据
    public boolean write(String string) {
        Log.e("write", string);
        if (writeChara != null) {
            writeChara.setValue(hexStringToBytes(string));
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
            //s  LinkBluetooth(address);
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
        Log.e("getData1", byteToHex(data[0]));
        if (byteToHex(data[0]).toLowerCase().equals("fe")) {
            byte[] bytes = Constant.jiemi(data);
            Log.e("getData1", byteToHex(bytes[1]));
            Intent intent = new Intent();
            switch (bytes[1]) {
                case 0x11:
                    //通讯权限获取
                    if (bytes[3] == 1) {
                        handler.removeCallbacks(runnable2);
                        intent.setAction(SUCCESSFUL_DEVICE_CONNECTION);
                        context.sendBroadcast(intent);
                        MyApplication.newInstance().KEY = byteToHex(bytes[4]);
                        MyApplication.newInstance().bindMac=address;
                        //配置APP的设置
                        handler.postDelayed(runnable, 500);
                    } else {
                        intent.setAction(EQUIPMENT_DISCONNECTED);
                        context.sendBroadcast(intent);
                    }
                    break;
                case 0x10:
                    intent.setAction(EQUIPMENT_DISCONNECTED);
                    context.sendBroadcast(intent);
                    break;
                case 0x12:
                    intent.setAction(ACTION_BLE_KEY_OPERATE_SUCCESSFULLY);
                    context.sendBroadcast(intent);
                    break;
                case 0x13:
                    break;
                case 0x14:
                    Log.e("getData1", bytesToHex(bytes));
                    if (bytes[3] == 2) {
                        intent.setAction(ACTION_BLE_DEVICE_LINK_WEB);
                        context.sendBroadcast(intent);
                    } else if (bytes[3] == 3) {
                        intent.setAction(ACTION_BLE_DEVICE_UNLINK_WEB);
                        context.sendBroadcast(intent);
                    }
                    break;
                case 0x31:
                    byteToHex(bytes[3]);//电量
                    byteToHex(bytes[4]);//防盗开关
                    byteToHex(bytes[5]);//锁车状态
                    byteToHex(bytes[6]);//震动开关
                    byteToHex(bytes[7]);//震动等级
                    byteToHex(bytes[8]);//gps
                    bytesToHex(new byte[]{bytes[9], bytes[10]});//定位间隔
                    byteToHex(bytes[11]);//定位间隔
                    byteToHex(bytes[12]);//主版本号
                    byteToHex(bytes[13]);//次版本号
                    byteToHex(bytes[14]);//版本修改次数
                    break;
                case 0x21:
                    byteToHex(bytes[3]);//开锁状态
                    break;
                case 0x36:
                    //设置电量
                    break;
                case (byte) 0xFA:
                    //可以获取设备信息
                    size = Integer.parseInt(ToHex.bytesToHex(new byte[]{bytes[3], bytes[4]}), 16);//数据包总数
                    Log.e("size", " " + size);
                    type = byteToHex(bytes[7]);
                    String key = MyApplication.newInstance().KEY + "FB030000" + byteToHex(bytes[7]);
                    indext++;
                    write(Constant.jiami("FE", ToHex.random(), key));
                    break;
                case (byte) 0xFD:
                    //收到硬件要获取的第几包数据
                    String indext = String.valueOf(Integer.parseInt(ToHex.bytesToHex(new byte[]{bytes[3], bytes[4]}), 16));
                  //  if (size < hashMap.size()) {
                        byte[] bt = hashMap.get(indext);
                        String msg = ToHex.StringToHex3(indext) + ToHex.bytesToHex(bt);
                        msg = bytesToHex(CRCUtil.returnCRC2(ToHex.hexStringToBytes(msg))) + msg;
                        write(msg);
                        Log.e("写入", "第" + indext + "包" + msg);
                        //size = Integer.parseInt(indext);
                  //  }else {
                        //写入完成
                       // intent.setAction(ACTION_DATA);
                       // context.sendBroadcast(intent);
                   // }

                    break;
                default:
                    break;
            }
        } else {
            Log.e("getData", ToHex.bytesToHex(data));
            if (CheckCRC(data)) {
                getDeviceInfo(data);
                //获取到设备信息
                if (indext < size) {
                    Log.e("size", indext + " " + size);
                    String msg = MyApplication.newInstance().KEY + "FB03" + ToHex.StringToHex3(indext + "") + type;
                    write(Constant.jiami("FE", ToHex.random(), msg));
                    indext++;
                } else {
                    byte[] bytes = new byte[mList.size()];
                    for (int i = 0; i < mList.size(); i++) {
                        bytes[i] = mList.get(i);
                    }
                    String msg = new String(bytes);
                    Log.e("设备信息", msg);
                    Intent intent = new Intent();
                    intent.setAction(ACTION_BLE_NOTIFY_DATA);
                    intent.putExtra(DEVICE_INFO, msg);
                    intent.putExtra("type", type);
                    context.sendBroadcast(intent);
                    indext = 0;
                    size = 0;
                    type = "";
                    info = "";
                }
            } else {
                //CRC校验失败重获取上一包数据
                indext--;
                String msg = MyApplication.newInstance().KEY + "FB03" + ToHex.StringToHex3(indext + "") + type;
                write(Constant.jiami("FE", ToHex.random(), msg));
            }
        }
    }

    int indext = 0;
    String type = "";
    String info = "";
    List<Byte> mList = new ArrayList<>();

    private void getDeviceInfo(byte[] data) {

        for (int i = 4, j = 0; i < data.length; i++, j++) {
            if (data[i] > 0)
                mList.add(data[i]);

        }

    }

    public void setHashMap(Map<String, byte[]> hashMap) {
        this.hashMap = hashMap;
    }

    private void getService(BluetoothGatt gatt) {
        for (BluetoothGattService bluetoothGattService : gatt.getServices()) {
            Log.d("getService", bluetoothGattService.getUuid().toString());
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                Log.d("Characteristic", bluetoothGattCharacteristic.getUuid().toString());
            }
        }
    }
}
