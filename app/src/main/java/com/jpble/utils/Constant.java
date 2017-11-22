package com.jpble.utils;


import android.util.Log;

import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.jpble.ble.CRCUtil.returnCRC;
import static com.jpble.utils.ToHex.hexStringToBytes;

/**
 * <br />
 * created by  at 2017/1/12 11:48.
 */

public class Constant {

    //设备连接成功广播
    public static final String SUCCESSFUL_DEVICE_CONNECTION = "com.jpble.SUCCESSFUL_DEVICE_CONNECTION";
    //断开
    public static final String EQUIPMENT_DISCONNECTED = "com.jpble.EQUIPMENT_DISCONNECTED";
    //发送设备数据广播
    public static final String ACTION_BLE_NOTIFY_DATA = "ccom.jpble.ACTION_BLE_NOTIFY_DATA";

    //指令操作广播
    public static final String ACTION_BLE_KEY_OPERATE_SUCCESSFULLY = "com.jpble.ACTION_BLE_KEY_OPERATE_SUCCESSFULLY";
    public static final String ACTION_BLE_KEY_OPERATION_FAILURE = "com.jpble.ACTION_BLE_KEY_OPERATION_FAILURE";

    public final static String EXTRA_STATUS = "com.pgt.pedelec.EXTRA_STATUS";
    public final static String TRACKING_INTERVAL = "TrackingInterval";//追踪时间
    public final static String GPS = "GPS";//gps
    public final static String VIBRATION_LEVEL = "VibrationLevel";//震动等级
    public final static String VIBRATION_SWITCH = "VibrationSwitch";//震动开关
    public final static String LOCK_STATUS = "LockStatus";//锁车状态
    public final static String SECURITY_SWITCH = "SecuritySwitch";//防盗开关


    /**
     * 协议服务的UUID
     */
    public final static UUID UUID_SERVICE = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7");
    /**
     * 控制功能, write 属性<br />
     */
    public final static UUID UUID_CHARACTERISTIC_CONTROL = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cba");

    /**
     * 实时数据，notify属性<br />
     */
    public final static UUID UUID_CHARACTERISTIC_NOTIFY_DATA = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb8");

    public static String getData(String msg) {
        byte[] data;
        data = returnCRC(hexStringToBytes(msg));
        Log.e("TAG", ToHex.bytesToHex(data));

        return ToHex.bytesToHex(data);
    }

    public static String jiami(String tou, int num, String msg) {
        byte num1 = (byte) ((byte) num + 0x32);
        byte[] data = hexStringToBytes(msg);
        byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            bytes[i] = (byte) (data[i] ^ (byte) num);
            Log.e(TAG, bytes[i] + "");
        }
        String msg2 = getData(tou + ToHex.byteToHex(num1) + ToHex.bytesToHex(bytes));
        Log.e("jiami", ToHex.bytesToHex(bytes) + " " + ToHex.byteToHex(num1) + " " + msg2);
        return msg2;
    }

    public static  byte[] jiemi(byte[] msg) {
        byte[] data = new byte[msg.length - 4];
        byte num = (byte) (msg[1] - 0x32);
        for (int i = 2, j = 0; i < msg.length - 2; i++, j++) {
            data[j] = (byte) (msg[i] ^ num);
        }
        Log.e("jiemi", ToHex.bytesToHex(data));
        return data;
    }
}
