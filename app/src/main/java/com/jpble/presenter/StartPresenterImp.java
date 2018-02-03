package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.StartModelImp;
import com.jpble.view.CodeView;

/**
 * 作者：omni20170501
 */

public class StartPresenterImp extends BasePresenterImp<CodeView,Code> {


    private Context context = null;
    private StartModelImp startModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public StartPresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        startModelImp=new StartModelImp(context);
    }

    public void register(String url, String token, String lockId) {
        startModelImp.loadCode(url,token,lockId,this);
    }


    public void unSubscribe() {
        startModelImp.onUnsubscribe();
    }
}
