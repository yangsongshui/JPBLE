package com.jpble.bean;

/**
 * 作者：omni20170501
 */

public class Device {
    private String name;
    private String mac;
    private boolean isBle;
    private boolean isLink;

    public Device() {
    }

    public Device(String name, boolean isBle, boolean isLink) {
        this.name = name;
        this.isBle = isBle;
        this.isLink = isLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isBle() {
        return isBle;
    }

    public void setBle(boolean ble) {
        isBle = ble;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean link) {
        isLink = link;
    }
}
