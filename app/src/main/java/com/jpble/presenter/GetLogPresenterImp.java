package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.LogInfo;
import com.jpble.model.GetLogModelImp;
import com.jpble.view.LogInfoView;

/**
 * 作者：omni20170501
 */

public class GetLogPresenterImp extends BasePresenterImp<LogInfoView,LogInfo> {


    private Context context = null;
    private GetLogModelImp getLogModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public GetLogPresenterImp(LogInfoView view, Context context) {
        super(view);
        this.context=context;
        getLogModelImp=new GetLogModelImp(context);
    }


    public void register(String token ) {
        getLogModelImp.loadCode(token,this);
    }


    public void unSubscribe() {
        getLogModelImp.onUnsubscribe();
    }
}
