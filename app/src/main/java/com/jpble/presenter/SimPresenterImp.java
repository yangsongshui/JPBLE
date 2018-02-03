package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Sim;
import com.jpble.model.SimModelImp;
import com.jpble.view.SimView;

/**
 * 作者：omni20170501
 */

public class SimPresenterImp extends BasePresenterImp<SimView,Sim> {


    private Context context = null;
    private SimModelImp simModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public SimPresenterImp(SimView view, Context context) {
        super(view);
        this.context=context;
        simModelImp=new SimModelImp(context);
    }


    public void getDevice(String token ) {
        simModelImp.loadCode(token,this);
    }


    public void unSubscribe() {
        simModelImp.onUnsubscribe();
    }
}
