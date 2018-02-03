package com.jpble.bean;

/**
 * 作者：omni20170501
 */

public class AddCode {


    /**
     * code : 200
     * data : {"lockId":10,"result":1}
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
         * lockId : 10
         * result : 1
         */

        private int lockId;
        private int result;

        public int getLockId() {
            return lockId;
        }

        public void setLockId(int lockId) {
            this.lockId = lockId;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}
