package com.jpble.ble;

import java.io.Serializable;

/**
 * Created by ys on 2017/7/12.
 */

public class DeviceState implements Serializable {
    String on;
    String off;
    String key;
    String led;
    String lockset;
    String voltage;

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

    public String getLockset() {
        return lockset;
    }

    public void setLockset(String lockset) {
        this.lockset = lockset;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }
}
