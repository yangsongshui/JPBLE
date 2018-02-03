package com.jpble.ble;

import java.io.Serializable;

/**
 * Created by ys on 2017/7/12.
 */

public class DeviceState implements Serializable {
    int dianchi;
    String kaiguan;
    String zhuangtai;
    String zhendong;
    String dengji;
    String time;
    String banben;
    String banben2;

    public int getDianchi() {
        return dianchi;
    }

    public void setDianchi(int dianchi) {
        this.dianchi = dianchi;
    }

    public String getKaiguan() {
        return kaiguan;
    }

    public void setKaiguan(String kaiguan) {
        this.kaiguan = kaiguan;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getZhendong() {
        return zhendong;
    }

    public void setZhendong(String zhendong) {
        this.zhendong = zhendong;
    }

    public String getDengji() {
        return dengji;
    }

    public void setDengji(String dengji) {
        this.dengji = dengji;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBanben() {
        return banben;
    }

    public void setBanben(String banben) {
        this.banben = banben;
    }

    public String getBanben2() {
        return banben2;
    }

    public void setBanben2(String banben2) {
        this.banben2 = banben2;
    }

    @Override
    public String toString() {
        return "DeviceState{" +
                "dianchi=" + dianchi +
                ", kaiguan='" + kaiguan + '\'' +
                ", zhuangtai='" + zhuangtai + '\'' +
                ", zhendong='" + zhendong + '\'' +
                ", dengji='" + dengji + '\'' +
                ", time='" + time + '\'' +
                ", banben='" + banben + '\'' +
                ", banben2='" + banben2 + '\'' +
                '}';
    }
}
