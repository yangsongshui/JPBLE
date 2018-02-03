package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.ble.DeviceInfo;
import com.jpble.model.GetRideModelImp;
import com.jpble.view.RideMsgView;

/**
 * 作者：omni20170501
 */

public class GetRidePresenterImp extends BasePresenterImp<RideMsgView, DeviceInfo> {


    private Context context = null;
    private GetRideModelImp getRideModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public GetRidePresenterImp(RideMsgView view, Context context) {
        super(view);
        this.context = context;
        getRideModelImp = new GetRideModelImp(context);
    }


    public void register(String token) {
        getRideModelImp.loadCode(token, this);
    }


    public void unSubscribe() {
        getRideModelImp.onUnsubscribe();
    }
}
