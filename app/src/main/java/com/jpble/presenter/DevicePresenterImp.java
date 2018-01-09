package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Device;
import com.jpble.model.DeviceModelImp;
import com.jpble.view.DeviceView;

/**
 * 作者：omni20170501
 */

public class DevicePresenterImp extends BasePresenterImp<DeviceView,Device> {


    private Context context = null;
    private DeviceModelImp deviceModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public DevicePresenterImp(DeviceView view, Context context) {
        super(view);
        this.context=context;
        deviceModelImp=new DeviceModelImp(context);
    }


    public void getDevice(String token ) {
        deviceModelImp.loadCode(token,this);
    }


    public void unSubscribe() {
        deviceModelImp.onUnsubscribe();
    }
}
