package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Log;
import com.jpble.model.GetInfoModelImp;
import com.jpble.view.LogView;

/**
 * 作者：omni20170501
 */

public class GetInfoPresenterImp extends BasePresenterImp<LogView, Log> {


    private Context context = null;
    private GetInfoModelImp getInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public GetInfoPresenterImp(LogView view, Context context) {
        super(view);
        this.context = context;
        getInfoModelImp = new GetInfoModelImp(context);
    }


    public void register(String url, String orderId, String token) {
        getInfoModelImp.loadCode(url, orderId, token, this);
    }


    public void unSubscribe() {
        getInfoModelImp.onUnsubscribe();
    }
}
