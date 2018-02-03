package com.jpble.bean;

/**
 * 作者：omni20170501
 */

public class DeviceMsg {

    /**
     * data : {"id":12,"number":444444,"imei":861715030348461,"mac":"D7:01:82:2B:7E:1B","uid":10,"password":"GptZWUQkXDr4uqF7OUXSVg==","antiTheft":0,"shake":0,"shakeLevel":0,"shakeTime":0,"tracking":0,"trackingIntervel":0,"power":398,"status":0,"gLng":114.1256185,"gLat":22.634220166666665,"gTime":1516789730,"heartTime":0,"gsm":0,"version":null,"versionTime":null,"iccid":null,"name":"My Geo","userVo":{"id":null,"nickname":null,"password":null,"phone":null,"email":"1875044238@qq.com","avatar":null,"registerDate":null,"loginDate":null},"powerPercent":0,"statusStr":null,"gTimeStr":"2018-01-24 10:28:50.0","heartTimeStr":"1970-01-01 00:00:00.0"}
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
         * id : 12
         * number : 444444
         * imei : 861715030348461
         * mac : D7:01:82:2B:7E:1B
         * uid : 10
         * password : GptZWUQkXDr4uqF7OUXSVg==
         * antiTheft : 0
         * shake : 0
         * shakeLevel : 0
         * shakeTime : 0
         * tracking : 0
         * trackingIntervel : 0
         * power : 398
         * status : 0
         * gLng : 114.1256185
         * gLat : 22.634220166666665
         * gTime : 1516789730
         * heartTime : 0
         * gsm : 0
         * version : null
         * versionTime : null
         * iccid : null
         * name : My Geo
         * userVo : {"id":null,"nickname":null,"password":null,"phone":null,"email":"1875044238@qq.com","avatar":null,"registerDate":null,"loginDate":null}
         * powerPercent : 0.0
         * statusStr : null
         * gTimeStr : 2018-01-24 10:28:50.0
         * heartTimeStr : 1970-01-01 00:00:00.0
         */

        private int id;
        private int number;
        private long imei;
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
        private UserVoBean userVo;
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

        public long getImei() {
            return imei;
        }

        public void setImei(long imei) {
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

        public UserVoBean getUserVo() {
            return userVo;
        }

        public void setUserVo(UserVoBean userVo) {
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

        public static class UserVoBean {
            /**
             * id : null
             * nickname : null
             * password : null
             * phone : null
             * email : 1875044238@qq.com
             * avatar : null
             * registerDate : null
             * loginDate : null
             */

            private Object id;
            private Object nickname;
            private Object password;
            private Object phone;
            private String email;
            private Object avatar;
            private Object registerDate;
            private Object loginDate;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public Object getNickname() {
                return nickname;
            }

            public void setNickname(Object nickname) {
                this.nickname = nickname;
            }

            public Object getPassword() {
                return password;
            }

            public void setPassword(Object password) {
                this.password = password;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public Object getAvatar() {
                return avatar;
            }

            public void setAvatar(Object avatar) {
                this.avatar = avatar;
            }

            public Object getRegisterDate() {
                return registerDate;
            }

            public void setRegisterDate(Object registerDate) {
                this.registerDate = registerDate;
            }

            public Object getLoginDate() {
                return loginDate;
            }

            public void setLoginDate(Object loginDate) {
                this.loginDate = loginDate;
            }
        }
    }
}
