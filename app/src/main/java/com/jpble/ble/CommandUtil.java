package com.jpble.ble;

import android.util.Log;

import com.jpble.app.MyApplication;

import java.util.Arrays;
import java.util.Random;

/**
 * <br />
 * created by CxiaoX at 2017/4/22 18:02.
 */

public class CommandUtil {
    private  static  final  byte ORDER_KEY=0x11;
    private  static  final  byte ORDER_UN_LOCK=0x21;
    private  static  final  byte ORDER_LOCK=0x22;
    private  static  final  byte ORDER_FENCE=0x23;
    private  static  final  byte ORDER_LOCK_STATUS=0x31;
    private  static  final  byte ORDER_OLE_DATA=0x51;
    private  static  final  byte ORDER_CLEAR_OLE_DATA=0x52;
    private  static  final  byte ORDER_POPUP= (byte) 0x81;

    /**
     * 获取到 开锁指令的原指令，没有异或，没有RCR校验位
     * @param uid
     * @param bleKey
     * @return
     */
    public static byte[]   getOpenCommand(int uid,byte bleKey,long timestamp){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);
        byte uidB1=(byte) (( uid>>24)&0xFF);
        byte uidB2=(byte) (( uid>>16)&0xFF);
        byte uidB3=(byte) ( ( uid>>8)&0xFF);
        byte uidB4=(byte) ( uid &0xFF);

        byte time1=(byte) (( timestamp>>24)&0xFF);
        byte time2=(byte) (( timestamp>>16)&0xFF);
        byte time3=(byte) ( ( timestamp>>8)&0xFF);
        byte time4=(byte) ( timestamp &0xFF);
        byte[] command=new byte[13];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1

        command[2]= bleKey;  // key
        command[3]= ORDER_UN_LOCK;  // 命令代码
        command[4]= 0x08;  // 长度
        command[5]= uidB1; // 用户id
        command[6]= uidB2;
        command[7]= uidB3;
        command[8]= uidB4;
        command[9]= time1; // 用户id
        command[10]= time2;
        command[11]= time3;
        command[12]= time4;
        return command ;
    }
    public static byte[]   getBGFOpenCommand(int uid,byte bleKey,long timestamp,int openType){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);
        byte uidB1=(byte) (( uid>>24)&0xFF);
        byte uidB2=(byte) (( uid>>16)&0xFF);
        byte uidB3=(byte) ( ( uid>>8)&0xFF);
        byte uidB4=(byte) ( uid &0xFF);

        byte time1=(byte) (( timestamp>>24)&0xFF);
        byte time2=(byte) (( timestamp>>16)&0xFF);
        byte time3=(byte) ( ( timestamp>>8)&0xFF);
        byte time4=(byte) ( timestamp &0xFF);
        byte[] command=new byte[14];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1

        command[2]= bleKey;  // key
        command[3]= ORDER_UN_LOCK;  // 命令代码
        command[4]= 0x09;  // 长度
        command[5]= uidB1; // 用户id
        command[6]= uidB2;
        command[7]= uidB3;
        command[8]= uidB4;
        command[9]= time1; // 开锁时间戳
        command[10]= time2;
        command[11]= time3;
        command[12]= time4;
        command[13]= (byte)(openType&0xFF);
        return command ;
    }

    public static byte[]   getCarportLockCommand(int uid,byte bleKey,long timestamp,int openType){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);
        byte uidB1=(byte) (( uid>>24)&0xFF);
        byte uidB2=(byte) (( uid>>16)&0xFF);
        byte uidB3=(byte) ( ( uid>>8)&0xFF);
        byte uidB4=(byte) ( uid &0xFF);

        byte time1=(byte) (( timestamp>>24)&0xFF);
        byte time2=(byte) (( timestamp>>16)&0xFF);
        byte time3=(byte) ( ( timestamp>>8)&0xFF);
        byte time4=(byte) ( timestamp &0xFF);
        byte[] command=new byte[14];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1

        command[2]= bleKey;  // key
        command[3]= ORDER_UN_LOCK;  // 命令代码
        command[4]= 0x09;  // 长度
        command[5]= uidB1; // 用户id
        command[6]= uidB2;
        command[7]= uidB3;
        command[8]= uidB4;
        command[9]= time1; // 开锁时间戳
        command[10]= time2;
        command[11]= time3;
        command[12]= time4;
        command[13]= (byte)(openType&0xFF);
        return command ;
    }

    private static  final  String TAG="CommandUtil";
    /**
     * 获取到 经过 异或 加密 和 CRC 校验后的开锁指令
     * @param uid
     * @param bleKey
     * @return
     */
    public static byte[] getCRCOpenCommand(int uid,byte bleKey,long timestamp){
        byte[] command = getOpenCommand( uid, bleKey,timestamp);
        Log.i(TAG, "getCRCOpenCommand: cmm="+ Arrays.toString(command));
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        Log.i(TAG, "getCRCOpenCommand: CRC cmm="+ Arrays.toString(command));
        return  crcOrder;
    }
    public static byte[] getCRCBGFOpenCommand(int uid,byte bleKey,long timestamp,int openType){
        byte[] command = getBGFOpenCommand( uid, bleKey,timestamp,openType);
        Log.i(TAG, "getCRCOpenCommand: cmm="+ Arrays.toString(command));
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        Log.i(TAG, "getCRCOpenCommand: CRC cmm="+ Arrays.toString(command));
        return  crcOrder;
    }
    public static byte[] getCRCCarportLockCommand(int uid,byte bleKey,long timestamp,int openType){
        byte[] command = getCarportLockCommand( uid, bleKey,timestamp,openType);
        Log.i(TAG, "getCRCOpenCommand: cmm="+ Arrays.toString(command));
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        Log.i(TAG, "getCRCOpenCommand: CRC cmm="+ Arrays.toString(command));
        return  crcOrder;
    }
    /**
     * 获取KEY指令的 原指令，没有异或，没有RCR校验位
     * @param uid
     * @param bleKey
     * @return
     */
    public static byte[]   getKeyCommand(int uid,byte bleKey){
        return getCommand(uid,bleKey,ORDER_KEY,(byte)0x00);
    }


    public static byte[] getCRCKeyCommand2( ){
        byte[] command = getKeyCommand2(   );
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }
    /**
     * 给锁的回复信息， 收到了锁发送到app的上锁指令
     * @param uid
     * @param bleKey
     * @return
     */
    private static byte[]   getLockCommand(byte bleKey){
        return getCommand(bleKey,ORDER_LOCK,(byte)0x00);
    }

    private static byte[]   getFenceCommand(byte bleKey){
        return getCommand(bleKey,ORDER_FENCE,(byte)0x00);
    }
    public static byte[] getCRCLockCommand(byte bleKey){
        byte[] command = getLockCommand(  bleKey);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }

    public static byte[] getCRCFenceCommand(byte bleKey){
        byte[] command = getFenceCommand(  bleKey);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }
    public static byte[] getCRCBGFLockCommand(byte bleKey){
        byte[] command = getLockCommand(  bleKey);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }

    public static byte[] getCRCOpenResCommand(byte bleKey){
        byte[] command = getCommand(  bleKey,ORDER_UN_LOCK,(byte)0x00);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }

    public static byte[] getCRCUpRespCommand( byte blekey,byte order,byte resp){
        byte[] command = getUpResCommand(  blekey,order,resp);
        byte[] xorCommand=decode(command);
        return CRCByte(xorCommand);

    }

    public static byte[]   getLockStatusCommand(byte bleKey){
        return getCommand(bleKey,ORDER_LOCK_STATUS,(byte)0x00);
    }
    public static byte[]   getCRCLockStatusCommand(byte bleKey){
        byte[] command = getLockStatusCommand( bleKey);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }
    public static byte[]   geOldDataCommand( byte bleKey){
        return getCommand( bleKey,ORDER_OLE_DATA,(byte)0x00);
    }
    public static byte[]   getCRCOldDataCommand( byte bleKey){
        byte[] command = geOldDataCommand(   bleKey);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }
    public static byte[]   geClearDataCommand( byte bleKey){
        return getCommand(bleKey,ORDER_CLEAR_OLE_DATA,(byte)0x00);
    }
    public static byte[]   getCRCClearDataCommand( byte bleKey){
        // 原
        byte[] command = geClearDataCommand(   bleKey);
        // 加密
        byte[] xorCommand=decode(command);
        // CRC
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }

    public static byte[]   getPopupCommand( byte bleKey){
        return getCommand( bleKey,ORDER_POPUP,(byte)0x00);
    }
    public static byte[]   getCRCPopupDataCommand( byte bleKey){
        byte[] command = getPopupCommand( bleKey);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }

    public static byte[] getCRCUpCommand(byte bleKey,int nPack,int upCRC,byte dType){
        byte[] command = getUpCommand(  bleKey, nPack, upCRC, dType);
        byte[] xorCommand=decode(command);
        byte[] crcOrder= CRCByte(xorCommand);
        return  crcOrder;
    }

    public static byte[] getCRCSetCommand(byte bleKey,int nPack,int upCRC,byte dType){
        byte[] command = getSetCommand( bleKey, nPack, upCRC, dType);
        byte[] xorCommand=decode(command);
        return CRCByte(xorCommand);
    }

    public static byte[] getCRCReadInfoCommand( byte bleKey,byte order,byte len){
        byte[] command = getCommand(  bleKey, order,len);
        byte[] xorCommand=decode(command);
        return CRCByte(xorCommand);
    }
    public static byte[] getCRCCloseCommand( byte bleKey,byte order,byte len){
        byte[] command = getCommand(  bleKey, order,len);
        byte[] xorCommand=decode(command);
        return CRCByte(xorCommand);
    }
    public static byte[] getCRCInfoDetailComm(byte bleKey,int nPack,byte dType){

        byte[] command = getInfoDetailCommand(   bleKey, nPack,dType);
        byte[] xorCommand=decode(command);
        return CRCByte(xorCommand);
    }

    private static byte[]   getCommand(int uid,byte bleKey,byte order,byte len){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);
        byte uidB1=(byte) (( uid>>24)&0xFF);
        byte uidB2=(byte) (( uid>>16)&0xFF);
        byte uidB3=(byte) ( ( uid>>8)&0xFF);
        byte uidB4=(byte) ( uid &0xFF);
        byte[] command=new byte[9];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1
        command[2]= uidB1; // 用户id
        command[3]= uidB2;
        command[4]= uidB3;
        command[5]= uidB4;
        command[6]= bleKey;  // key
        command[7]= order;  // 命令代码
        command[8]= len;  // 长度
        return command;
    }
    private static byte[]   getCommand(byte bleKey,byte order,byte len){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);

        byte[] command=new byte[5];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1
        command[2]= bleKey;  // key
        command[3]= order;  // 命令代码
        command[4]= len;  // 长度
        return command;
    }
    private static byte[]   getKeyCommand2(  ){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);

        byte[] command=new byte[13];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1
        command[2]= 0;  // key
        command[3]= ORDER_KEY;  // 命令代码

        command[4]= 0x08;  // 长度
        // yOTmK50z
        // 0x79 0x4F 0x54 0x6D 0x4B 0x35 0x30 0x7A
//        String  deviceKey="OMni8ZH7";
        //fLhx11vA
        String  deviceKey= MyApplication.newInstance().deviceKey;
        for(int i=0;i<deviceKey.length();i++){
            command[5+i]= (byte) deviceKey.charAt(i);
        }
        Log.i(TAG, "getUpCommand:   deviceKey="+deviceKey);
        return  command;
    }

    private static byte[]   getUpResCommand( byte bleKey,byte order,byte resp){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);

        byte[] command=new byte[6];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1

        command[2]= bleKey;  // key
        command[3]= order;  // 命令代码
        command[4]= 0x01;  // 长度
        command[5]=resp; // 设备类型
        return command;
    }


    private static byte[]   getInfoDetailCommand( byte bleKey,int nPack,byte dType){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);

        byte[] command=new byte[8];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1

        command[2]= bleKey;  // key
        command[3]= (byte)0xFB;  // 命令代码
        command[4]= 0x03;  // 长度
        command[5]= (byte)((nPack>>8)&0xFF);
        command[6]= (byte) (nPack & 0xFF);
        command[7]=dType;
        return command;
    }

    private static byte[]   getUpCommand( byte bleKey,int nPack,int upCRC,byte dType){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);

        byte[] command=new byte[14];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1

        command[2]= bleKey;  // key
        command[3]= (byte) 0xF0;  // 命令代码
        command[4]= 0x09;  // 长度
        command[5]= (byte) ((nPack>>8)&0xFF);
        command[6]= (byte) (nPack&0xFF);
        command[7]= (byte) ((upCRC>>8)&0xFF);
        command[8]= (byte) (upCRC&0xFF);
        command[9]= dType;
        //Vgz7
        // 0x56 0x67 0x7A 0x37
//        String updateKey = "Hx8s";
        String updateKey = MyApplication.newInstance().updateKey;
        for(int i=0;i<updateKey.length();i++){
            command[10+i]=(byte)updateKey.charAt(i);
        }
        Log.i(TAG, "getUpCommand: update KEY="+updateKey);
        return command;
    }


    private static byte[]   getSetCommand(byte bleKey,int nPack,int upCRC,byte dType){
        byte randKey = (byte) (new Random().nextInt(255) & 0xff);
        byte[] command=new byte[10];// 不包含CRC校验的长度
        command[0]= (byte) 0xFE;
        command[1]=(byte) (randKey); //随机数 x1
        command[2]= bleKey;  // key
        command[3]= (byte) 0xFC;  // 命令代码
        command[4]= 0x05;  // 长度
        command[5]= (byte) ((nPack>>8)&0xFF);
        command[6]= (byte) (nPack&0xFF);
        command[7]= (byte) ((upCRC>>8)&0xFF);
        command[8]= (byte) (upCRC&0xFF);
        command[9]= dType;

        return command;
    }

    public static byte[] getCRCUpDetailCommand(int nPack,byte[] data){
        byte[] command = getUpDetailCommand( nPack, data);

        byte[] crcOrder= CRCByte2(command);
        return  crcOrder;
    }
    private static byte[]   getUpDetailCommand(int nPack,byte[] data){
        byte[] command=new byte[18];// 不包含CRC校验的长度
        command[0]= (byte) ((nPack>>8)&0xFF);
        command[1]= (byte) ((nPack )&0xFF);
        System.arraycopy(data,0,command,2,data.length);

        return command;


    }


    private static byte[] decode(byte[] command){
        byte[] xorComm = new byte[command.length];
        xorComm[0]=command[0];
        xorComm[1] =(byte) (command[1] +0x32);
        for(int i=2;i<command.length;i++){
            xorComm[i]= (byte) (command[i] ^ command[1]);
        }
        return xorComm;
    }

    private static byte[] CRCByte(byte[] ori){
        byte[] ret = new byte[ori.length+2];
        int crc = CRCUtil.calcCRC(ori);
        for(int i=0;i<ori.length;i++) ret[i]=ori[i];
        ret[ori.length]= (byte) ((crc>>8)&0xFF);
        ret[ori.length+1]=(byte)( crc &0xFF);
        return ret;
    }

    private static byte[] CRCByte2(byte[] ori){
        byte[] ret = new byte[ori.length+2];
        int crc = CRCUtil.calcCRC(ori);
//        Log.i(TAG, "CRCByte2: crc="+);

        ret[0]= (byte) ((crc>>8)&0xFF);
        ret[1]=(byte)( crc &0xFF);
        for(int i=0;i<ori.length;i++) ret[i+2]=ori[i];
        return ret;
    }
}
