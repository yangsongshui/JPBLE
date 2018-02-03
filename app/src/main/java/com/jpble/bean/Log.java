package com.jpble.bean;

/**
 * 作者：omni20170501
 */

public class Log {

    /**
     * data : {"id":59,"type":3,"imei":861715030349386,"content":"lat=22.634192166666665,lng=114.12572933333334","date":1513762090,"lockVo":{"id":null,"number":123456790,"imei":null,"mac":null,"uid":null,"password":null,"antiTheft":null,"shake":null,"shakeLevel":null,"shakeTime":null,"tracking":null,"trackingIntervel":null,"power":null,"status":null,"gLng":null,"gLat":null,"gTime":null,"heartTime":null,"gsm":null,"version":null,"versionTime":null,"iccid":null,"name":null,"userVo":null,"powerPercent":0,"statusStr":null,"gTimeStr":null,"heartTimeStr":null},"dateStr":"2017-12-20 17:28:10.0"}
     * code : 200
     */

    private DataBean data;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * id : 59
         * type : 3
         * imei : 861715030349386
         * content : lat=22.634192166666665,lng=114.12572933333334
         * date : 1513762090
         * lockVo : {"id":null,"number":123456790,"imei":null,"mac":null,"uid":null,"password":null,"antiTheft":null,"shake":null,"shakeLevel":null,"shakeTime":null,"tracking":null,"trackingIntervel":null,"power":null,"status":null,"gLng":null,"gLat":null,"gTime":null,"heartTime":null,"gsm":null,"version":null,"versionTime":null,"iccid":null,"name":null,"userVo":null,"powerPercent":0,"statusStr":null,"gTimeStr":null,"heartTimeStr":null}
         * dateStr : 2017-12-20 17:28:10.0
         */

        private int id;
        private int type;
        private long imei;
        private String content;
        private int date;
        private LockVoBean lockVo;
        private String dateStr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getImei() {
            return imei;
        }

        public void setImei(long imei) {
            this.imei = imei;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public LockVoBean getLockVo() {
            return lockVo;
        }

        public void setLockVo(LockVoBean lockVo) {
            this.lockVo = lockVo;
        }

        public String getDateStr() {
            return dateStr;
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }

        public static class LockVoBean {
            /**
             * id : null
             * number : 123456790
             * imei : null
             * mac : null
             * uid : null
             * password : null
             * antiTheft : null
             * shake : null
             * shakeLevel : null
             * shakeTime : null
             * tracking : null
             * trackingIntervel : null
             * power : null
             * status : null
             * gLng : null
             * gLat : null
             * gTime : null
             * heartTime : null
             * gsm : null
             * version : null
             * versionTime : null
             * iccid : null
             * name : null
             * userVo : null
             * powerPercent : 0.0
             * statusStr : null
             * gTimeStr : null
             * heartTimeStr : null
             */

            private Object id;
            private int number;
            private Object imei;
            private Object mac;
            private Object uid;
            private Object password;
            private Object antiTheft;
            private Object shake;
            private Object shakeLevel;
            private Object shakeTime;
            private Object tracking;
            private Object trackingIntervel;
            private Object power;
            private Object status;
            private Object gLng;
            private Object gLat;
            private Object gTime;
            private Object heartTime;
            private Object gsm;
            private Object version;
            private Object versionTime;
            private Object iccid;
            private Object name;
            private Object userVo;
            private double powerPercent;
            private Object statusStr;
            private Object gTimeStr;
            private Object heartTimeStr;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public Object getImei() {
                return imei;
            }

            public void setImei(Object imei) {
                this.imei = imei;
            }

            public Object getMac() {
                return mac;
            }

            public void setMac(Object mac) {
                this.mac = mac;
            }

            public Object getUid() {
                return uid;
            }

            public void setUid(Object uid) {
                this.uid = uid;
            }

            public Object getPassword() {
                return password;
            }

            public void setPassword(Object password) {
                this.password = password;
            }

            public Object getAntiTheft() {
                return antiTheft;
            }

            public void setAntiTheft(Object antiTheft) {
                this.antiTheft = antiTheft;
            }

            public Object getShake() {
                return shake;
            }

            public void setShake(Object shake) {
                this.shake = shake;
            }

            public Object getShakeLevel() {
                return shakeLevel;
            }

            public void setShakeLevel(Object shakeLevel) {
                this.shakeLevel = shakeLevel;
            }

            public Object getShakeTime() {
                return shakeTime;
            }

            public void setShakeTime(Object shakeTime) {
                this.shakeTime = shakeTime;
            }

            public Object getTracking() {
                return tracking;
            }

            public void setTracking(Object tracking) {
                this.tracking = tracking;
            }

            public Object getTrackingIntervel() {
                return trackingIntervel;
            }

            public void setTrackingIntervel(Object trackingIntervel) {
                this.trackingIntervel = trackingIntervel;
            }

            public Object getPower() {
                return power;
            }

            public void setPower(Object power) {
                this.power = power;
            }

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public Object getGLng() {
                return gLng;
            }

            public void setGLng(Object gLng) {
                this.gLng = gLng;
            }

            public Object getGLat() {
                return gLat;
            }

            public void setGLat(Object gLat) {
                this.gLat = gLat;
            }

            public Object getGTime() {
                return gTime;
            }

            public void setGTime(Object gTime) {
                this.gTime = gTime;
            }

            public Object getHeartTime() {
                return heartTime;
            }

            public void setHeartTime(Object heartTime) {
                this.heartTime = heartTime;
            }

            public Object getGsm() {
                return gsm;
            }

            public void setGsm(Object gsm) {
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

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getUserVo() {
                return userVo;
            }

            public void setUserVo(Object userVo) {
                this.userVo = userVo;
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

            public Object getGTimeStr() {
                return gTimeStr;
            }

            public void setGTimeStr(Object gTimeStr) {
                this.gTimeStr = gTimeStr;
            }

            public Object getHeartTimeStr() {
                return heartTimeStr;
            }

            public void setHeartTimeStr(Object heartTimeStr) {
                this.heartTimeStr = heartTimeStr;
            }
        }
    }
}
