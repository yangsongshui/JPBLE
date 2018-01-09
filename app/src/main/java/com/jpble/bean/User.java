package com.jpble.bean;

/**
 * 作者：omni20170501
 */

public class User {

    /**
     * code : 200
     * data : {"token":"FD6339A4D1B1D86C191E447A7FE3EA88CE97C6272485C9D76AB148DD31AB940B2C1FAB177C1701557A9D15C4D7F0C4C8","user":{"id":7,"phone":18664570155,"registerDate":"2017-12-26 16:43:27.0"}}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : FD6339A4D1B1D86C191E447A7FE3EA88CE97C6272485C9D76AB148DD31AB940B2C1FAB177C1701557A9D15C4D7F0C4C8
         * user : {"id":7,"phone":18664570155,"registerDate":"2017-12-26 16:43:27.0"}
         */

        private String token;
        private UserBean user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 7
             * phone : 18664570155
             * registerDate : 2017-12-26 16:43:27.0
             */

            private int id;
            private long phone;
            private String registerDate;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getPhone() {
                return phone;
            }

            public void setPhone(long phone) {
                this.phone = phone;
            }

            public String getRegisterDate() {
                return registerDate;
            }

            public void setRegisterDate(String registerDate) {
                this.registerDate = registerDate;
            }
        }

    }
}
