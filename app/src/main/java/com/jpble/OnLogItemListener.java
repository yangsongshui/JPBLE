package com.jpble;

import android.support.v7.widget.RecyclerView;

import com.jpble.bean.LogInfo;


/**
 * RecyclerView item监听
 * Created by omni20170501 on 2017/6/7.
 */

public interface OnLogItemListener {
    void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, LogInfo.DataBean bleDevice);

}
