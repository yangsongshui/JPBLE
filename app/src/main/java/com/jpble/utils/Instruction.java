package com.jpble.utils;

/**
 * 作者：omni20170501
 */

public class Instruction {
    public final static String BASESTX = "FE";
    /**
     * 系统操作指令
     */
    public static final String DATA = "01";
    public static final String DATA2 = "02";
    public static final String DATA3 = "03";
    public static final String DATA4 = "04";
    public static final String DATA5 = "05";
    public static final String DATA6 = "06";
        public static final String DATA7 = "07";
    public static final String DATA8 = "08";
    public static final String DATA9 = "09";
    public static final String DATA10 = "0A";
    public static final String DATA12 = "0B";
    public static final String DATA13 = "0C";
    /**
     * 用户管理指令
     */
    public static final String DATA20 = "20";
    public static final String DATA1A = "1A";
    public static final String DATA21 = "21";
    public static final String DATA22 = "22";
    public static final String DATA23 = "23";
    public static final String DATA24 = "24";
    public static final String DATA25 = "25";
    public static final String DATA26 = "26";
    public static final String DATA27 = "27";
    public static final String DATA28 = "28";
    public static final String DATA29 = "29";
    public static final String DATA30 = "2A";
    public static final String DATA31 = "2B";
    /**
     * 记录操作
     */
    public static final String DATA50 = "50";
    public static final String DATA51 = "51";
    public static final String DATA52 = "52";
    public static final String DATA53 = "53";
    /**
     * 获取通讯操作指令
     */

    public static final String PASSWORD = DATA + "1500";//获取操作权限
    public static final String PASSWORD2= DATA + "150100000000";//获取操作权限
    public static final String GETKEY = DATA2 + "10";//获取Key
    public static final String KAISUO = DATA3 + "12";//开锁
    public static final String ADMINPASSWORD = DATA4 + "14";//修改管理密码
    public static final String LOCK = DATA5 + "13";//修改管理密码
    public static final String SETALLGROUP = DATA7 + "2F0001";//设置组信息
    public static final String SETALLGROUP2 = DATA7 + "340002";//设置组信息
    public static final String TIME = DATA8 + "14";//同步时间
    public static final String RESTOREFACTORYSETTINGS  = DATA10 + "10";//恢复出厂设置
    public static final String SETVOLUME  = DATA9 + "11";//设置音量
    public static final String CANCEL  = DATA12 + "10";//取消操作
    public static final String GETVERSION  = DATA13 + "10";//获取电量和版本
    public static final String GETVERSION2 = DATA13 + "420002";//获取电量和版本

    public static final String ADDUSER = DATA20 + "2F0001";//添加用户
    public static final String ADDUSER2 = DATA20 + "2F0002";//添加用户后续包
    public static final String ADDUSER3= DATA20 + "350003";//添加用户后续包
    public static final String FINGERPRINT = DATA21 + "14";//登记指纹
    public static final String REGISTERCARD = DATA22 + "14";//登记卡
    public static final String UPADTEPSW = DATA23 + "16";//修改密码
    public static final String UPADTENAME = DATA24 + "2F0001";//修改昵称
    public static final String UPADTENAME2 = DATA24 + "2F0002";//修改昵称
    public static final String UPADTENAME3 = DATA24 + "330003";//修改昵称
    public static final String DELETEUSER = DATA25 + "12";//删除用户
    public static final String FORBIDDEN = DATA26 + "13";//启禁用
    public static final String JURISDICTION = DATA27 + "13";//更改权限
    public static final String SETGROUP = DATA28 + "13";//设置组
    public static final String GETUSER = DATA29 + "12";//获取用户信息
    public static final String ALLUSER = DATA30 + "10";//获取所有用户信息

    public static final String USERHISTORICAL = DATA50 + "12";//获取用户记录条数
    public static final String USERHISTORICALDATA = DATA51 + "18";//获取用户指定记录
    public static final String ALLHISTORICAL = DATA52 + "10";//获取 所有 记录条数
    public static final String ALLHISTORICALDATA = DATA53 + "18";//获取指定记录


}
