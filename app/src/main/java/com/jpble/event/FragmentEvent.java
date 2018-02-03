package com.jpble.event;


import com.jpble.ble.DeviceState;

/**
 * Created by ys on 2017/6/21.
 */

public class FragmentEvent {
    private DeviceState state;
    private int distance = 0;
    private int speed = 0;

    public FragmentEvent(DeviceState state) {

        this.state = state;
    }

    public FragmentEvent(int distance, int speed) {

        this.distance = distance;
    }

    public DeviceState getMsg() {
        return state;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDistance() {
        return distance;
    }
}
