package com.jpble.bean;

import java.util.List;

/**
 * 作者：omni20170501
 */

public class Device {


    /**
     * code : 200
     * data : [{"id":10,"number":123456790,"imei":123456790,"mac":"D7:01:82:2B:7E:1B","uid":7,"password":"0","antiTheft":0,"shake":0,"shakeLevel":0,"shakeTime":0,"tracking":0,"trackingIntervel":0,"power":0,"status":0,"gLng":0,"gLat":0,"gTime":0,"heartTime":0,"gsm":0,"version":null,"versionTime":null,"iccid":null,"name":"My Geo","powerPercent":0,"statusStr":null,"gTimeStr":"1970-01-01 08:00:00.0","heartTimeStr":"1970-01-01 08:00:00.0"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * number : 123456790
         * imei : 123456790
         * mac : D7:01:82:2B:7E:1B
         * uid : 7
         * password : 0
         * antiTheft : 0
         * shake : 0
         * shakeLevel : 0
         * shakeTime : 0
         * tracking : 0
         * trackingIntervel : 0
         * power : 0
         * status : 0
         * gLng : 0.0
         * gLat : 0.0
         * gTime : 0
         * heartTime : 0
         * gsm : 0
         * version : null
         * versionTime : null
         * iccid : null
         * name : My Geo
         * powerPercent : 0.0
         * statusStr : null
         * gTimeStr : 1970-01-01 08:00:00.0
         * heartTimeStr : 1970-01-01 08:00:00.0
         */

        private int id;
        private int number;
        private int imei;
        private String mac;
        private int uid;
        private String password;
        private int antiTheft;
        private int shake;
        private int shakeLevel;
        private int shakeTime;
        private int tracking;
        private int trackingIntervel;
        private int power;
        private int status;
        private double gLng;
        private double gLat;
        private int gTime;
        private int heartTime;
        private int gsm;
        private Object version;
        private Object versionTime;
        private Object iccid;
        private String name;
        private double powerPercent;
        private Object statusStr;
        private String gTimeStr;
        private String heartTimeStr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getImei() {
            return imei;
        }

        public void setImei(int imei) {
            this.imei = imei;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getAntiTheft() {
            return antiTheft;
        }

        public void setAntiTheft(int antiTheft) {
            this.antiTheft = antiTheft;
        }

        public int getShake() {
            return shake;
        }

        public void setShake(int shake) {
            this.shake = shake;
        }

        public int getShakeLevel() {
            return shakeLevel;
        }

        public void setShakeLevel(int shakeLevel) {
            this.shakeLevel = shakeLevel;
        }

        public int getShakeTime() {
            return shakeTime;
        }

        public void setShakeTime(int shakeTime) {
            this.shakeTime = shakeTime;
        }

        public int getTracking() {
            return tracking;
        }

        public void setTracking(int tracking) {
            this.tracking = tracking;
        }

        public int getTrackingIntervel() {
            return trackingIntervel;
        }

        public void setTrackingIntervel(int trackingIntervel) {
            this.trackingIntervel = trackingIntervel;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getGLng() {
            return gLng;
        }

        public void setGLng(double gLng) {
            this.gLng = gLng;
        }

        public double getGLat() {
            return gLat;
        }

        public void setGLat(double gLat) {
            this.gLat = gLat;
        }

        public int getGTime() {
            return gTime;
        }

        public void setGTime(int gTime) {
            this.gTime = gTime;
        }

        public int getHeartTime() {
            return heartTime;
        }

        public void setHeartTime(int heartTime) {
            this.heartTime = heartTime;
        }

        public int getGsm() {
            return gsm;
        }

        public void setGsm(int gsm) {
            this.gsm = gsm;
        }

        public Object getVersion() {
            return version;
        }

        public void setVersion(Object version) {
            this.version = version;
        }

        public Object getVersionTime() {
            return versionTime;
        }

        public void setVersionTime(Object versionTime) {
            this.versionTime = versionTime;
        }

        public Object getIccid() {
            return iccid;
        }

        public void setIccid(Object iccid) {
            this.iccid = iccid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPowerPercent() {
            return powerPercent;
        }

        public void setPowerPercent(double powerPercent) {
            this.powerPercent = powerPercent;
        }

        public Object getStatusStr() {
            return statusStr;
        }

        public void setStatusStr(Object statusStr) {
            this.statusStr = statusStr;
        }

        public String getGTimeStr() {
            return gTimeStr;
        }

        public void setGTimeStr(String gTimeStr) {
            this.gTimeStr = gTimeStr;
        }

        public String getHeartTimeStr() {
            return heartTimeStr;
        }

        public void setHeartTimeStr(String heartTimeStr) {
            this.heartTimeStr = heartTimeStr;
        }
    }
}
