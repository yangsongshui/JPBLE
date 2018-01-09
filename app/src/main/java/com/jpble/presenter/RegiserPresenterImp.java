package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.RegisterModelImp;
import com.jpble.view.CodeView;

import java.util.Map;

/**
 * 作者：omni20170501
 */

public class RegiserPresenterImp extends BasePresenterImp<CodeView,Code> implements RegisterPresenter{


    private Context context = null;
    private RegisterModelImp registerModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public RegiserPresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        registerModelImp=new RegisterModelImp(context);
    }

    @Override
    public void register(String url, Map<String, String> map) {
        registerModelImp.loadRegister(url,map,this);
    }

    @Override
    public void unSubscribe() {
        registerModelImp.onUnsubscribe();
    }
}
