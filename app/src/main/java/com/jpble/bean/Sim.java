package com.jpble.bean;

import java.util.List;

/**
 * 作者：omni20170501
 */

public class Sim {

    /**
     * code : 200
     * data : [{"account":"123456","apn":"unim2m","authType":"APN","expireDate":0,"expireDateStr":"1970-01-01 08:00","id":6,"lockId":10,"lockVo":{"id":10,"mac":"D7:01:82:2B:7E:1B","name":"My Geo","number":123456790,"password":"0","powerPercent":0},"number":"89860617030055791563","password":"amSW5xMaxOX1TYrq36V%2BLg%3D%3D","uid":10,"userVo":{"email":"1875044238@qq.com","id":10}}]
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
         * account : 123456
         * apn : unim2m
         * authType : APN
         * expireDate : 0
         * expireDateStr : 1970-01-01 08:00
         * id : 6
         * lockId : 10
         * lockVo : {"id":10,"mac":"D7:01:82:2B:7E:1B","name":"My Geo","number":123456790,"password":"0","powerPercent":0}
         * number : 89860617030055791563
         * password : amSW5xMaxOX1TYrq36V%2BLg%3D%3D
         * uid : 10
         * userVo : {"email":"1875044238@qq.com","id":10}
         */

        private String account;
        private String apn;
        private String authType;
        private int expireDate;
        private String expireDateStr;
        private int id;
        private int lockId;
        private LockVoBean lockVo;
        private String number;
        private String password;
        private int uid;
        private UserVoBean userVo;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getApn() {
            return apn;
        }

        public void setApn(String apn) {
            this.apn = apn;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public int getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(int expireDate) {
            this.expireDate = expireDate;
        }

        public String getExpireDateStr() {
            return expireDateStr;
        }

        public void setExpireDateStr(String expireDateStr) {
            this.expireDateStr = expireDateStr;
        }

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

        public LockVoBean getLockVo() {
            return lockVo;
        }

        public void setLockVo(LockVoBean lockVo) {
            this.lockVo = lockVo;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public UserVoBean getUserVo() {
            return userVo;
        }

        public void setUserVo(UserVoBean userVo) {
            this.userVo = userVo;
        }

        public static class LockVoBean {
            /**
             * id : 10
             * mac : D7:01:82:2B:7E:1B
             * name : My Geo
             * number : 123456790
             * password : 0
             * powerPercent : 0.0
             */

            private int id;
            private String mac;
            private String name;
            private int number;
            private String password;
            private double powerPercent;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public double getPowerPercent() {
                return powerPercent;
            }

            public void setPowerPercent(double powerPercent) {
                this.powerPercent = powerPercent;
            }
        }

        public static class UserVoBean {
            /**
             * email : 1875044238@qq.com
             * id : 10
             */

            private String email;
            private int id;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
