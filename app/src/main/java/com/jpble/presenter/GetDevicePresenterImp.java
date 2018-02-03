package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.DeviceMsg;
import com.jpble.model.GetDeviceModelImp;
import com.jpble.view.DeviceMsgView;

/**
 * 作者：omni20170501
 */

public class GetDevicePresenterImp extends BasePresenterImp<DeviceMsgView, DeviceMsg> {


    private Context context = null;
    private GetDeviceModelImp getDeviceModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public GetDevicePresenterImp(DeviceMsgView view, Context context) {
        super(view);
        this.context = context;
        getDeviceModelImp = new GetDeviceModelImp(context);
    }


    public void register(String url, String lockId, String token) {
        getDeviceModelImp.loadCode(url, lockId, token, this);
    }


    public void unSubscribe() {
        getDeviceModelImp.onUnsubscribe();
    }
}
