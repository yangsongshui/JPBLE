package com.jpble;

import android.support.v7.widget.RecyclerView;

import com.jpble.ble.BLEDevice;


/**
 * RecyclerView item监听
 * Created by omni20170501 on 2017/6/7.
 */

public interface OnItemCheckListener {
    void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, BLEDevice bleDevice);

}
