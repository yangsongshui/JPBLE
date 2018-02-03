package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.SecurityModelImp;
import com.jpble.view.CodeView;

import java.util.Map;

/**
 * 作者：omni20170501
 */

public class SecurityPresenterImp extends BasePresenterImp<CodeView,Code> {


    private Context context = null;
    private SecurityModelImp securityModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public SecurityPresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        securityModelImp=new SecurityModelImp(context);
    }

    public void register(String url, Map<String, String> map) {
        securityModelImp.loadCode(url,map,this);
    }


    public void unSubscribe() {
        securityModelImp.onUnsubscribe();
    }
}
