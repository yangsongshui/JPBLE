package com.jpble.bean;

/**
 * 作者：omni20170501
 */

public class Code {


    /**
     * code : 200
     * data : 1
     */

    private int code;
    private int data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Code{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
