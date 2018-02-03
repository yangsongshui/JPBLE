package com.jpble.ble;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ys on 2017/7/21.
 */

public class DeviceInfo  implements Serializable {

    /**
     * data : [{"id":9,"lockId":6,"uid":7,"startTime":1514518107,"endTime":1514526531,"speed":10,"maxSpeed":20,"date":1514518107,"orbit":"ogciCgerwT","userVo":{"id":7,"nickname":null,"password":null,"phone":null,"email":null,"avatar":null,"registerDate":null,"loginDate":null},"lockVo":{"id":6,"number":123456789,"imei":null,"mac":null,"uid":null,"password":null,"antiTheft":null,"shake":null,"shakeLevel":null,"shakeTime":null,"tracking":null,"trackingIntervel":null,"power":null,"status":null,"gLng":null,"gLat":null,"gTime":null,"heartTime":null,"gsm":null,"version":null,"versionTime":null,"iccid":null,"powerPercent":0,"statusStr":null,"gTimeStr":null,"heartTimeStr":null},"startTimeStr":"2017-12-29 11:28:27.0","endTimeStr":"2017-12-29 13:48:51.0","dateStr":"2017-12-29 11:28:27.0","distance":0.01}]
     * code : 200
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

    public static class DataBean  implements Serializable {
        /**
         * id : 9
         * lockId : 6
         * uid : 7
         * startTime : 1514518107
         * endTime : 1514526531
         * speed : 10.0
         * maxSpeed : 20.0
         * date : 1514518107
         * orbit : ogciCgerwT
         * userVo : {"id":7,"nickname":null,"password":null,"phone":null,"email":null,"avatar":null,"registerDate":null,"loginDate":null}
         * lockVo : {"id":6,"number":123456789,"imei":null,"mac":null,"uid":null,"password":null,"antiTheft":null,"shake":null,"shakeLevel":null,"shakeTime":null,"tracking":null,"trackingIntervel":null,"power":null,"status":null,"gLng":null,"gLat":null,"gTime":null,"heartTime":null,"gsm":null,"version":null,"versionTime":null,"iccid":null,"powerPercent":0,"statusStr":null,"gTimeStr":null,"heartTimeStr":null}
         * startTimeStr : 2017-12-29 11:28:27.0
         * endTimeStr : 2017-12-29 13:48:51.0
         * dateStr : 2017-12-29 11:28:27.0
         * distance : 0.01
         */

        private int id;
        private int lockId;
        private int uid;
        private int startTime;
        private int endTime;
        private double speed;
        private double maxSpeed;
        private int date;
        private String orbit;
        private UserVoBean userVo;
        private LockVoBean lockVo;
        private String startTimeStr;
        private String endTimeStr;
        private String dateStr;
        private double distance;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLockId() {
            return lockId;
        }

        public void setLockId(int lockId) {
            this.lockId = lockId;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getMaxSpeed() {
            return maxSpeed;
        }

        public void setMaxSpeed(double maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String getOrbit() {
            return orbit;
        }

        public void setOrbit(String orbit) {
            this.orbit = orbit;
        }

        public UserVoBean getUserVo() {
            return userVo;
        }

        public void setUserVo(UserVoBean userVo) {
            this.userVo = userVo;
        }

        public LockVoBean getLockVo() {
            return lockVo;
        }

        public void setLockVo(LockVoBean lockVo) {
            this.lockVo = lockVo;
        }

        public String getStartTimeStr() {
            return startTimeStr;
        }

        public void setStartTimeStr(String startTimeStr) {
            this.startTimeStr = startTimeStr;
        }

        public String getEndTimeStr() {
            return endTimeStr;
        }

        public void setEndTimeStr(String endTimeStr) {
            this.endTimeStr = endTimeStr;
        }

        public String getDateStr() {
            return dateStr;
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public static class UserVoBean implements Serializable{
            /**
             * id : 7
             * nickname : null
             * password : null
             * phone : null
             * email : null
             * avatar : null
             * registerDate : null
             * loginDate : null
             */

            private int id;
            private Object nickname;
            private Object password;
            private Object phone;
            private Object email;
            private Object avatar;
            private Object registerDate;
            private Object loginDate;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
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

        public static class LockVoBean implements Serializable{
            /**
             * id : 6
             * number : 123456789
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
             * powerPercent : 0.0
             * statusStr : null
             * gTimeStr : null
             * heartTimeStr : null
             */

            private int id;
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
            private double powerPercent;
            private Object statusStr;
            private Object gTimeStr;
            private Object heartTimeStr;

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

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", lockId=" + lockId +
                    ", uid=" + uid +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", speed=" + speed +
                    ", maxSpeed=" + maxSpeed +
                    ", date=" + date +
                    ", orbit='" + orbit + '\'' +
                    ", userVo=" + userVo +
                    ", lockVo=" + lockVo +
                    ", startTimeStr='" + startTimeStr + '\'' +
                    ", endTimeStr='" + endTimeStr + '\'' +
                    ", dateStr='" + dateStr + '\'' +
                    ", distance=" + distance +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
