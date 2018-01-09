package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.CodeModelImp;
import com.jpble.view.CodeView;

/**
 * 作者：omni20170501
 */

public class CodePresenterImp extends BasePresenterImp<CodeView,Code> {


    private Context context = null;
    private CodeModelImp codeModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public CodePresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        codeModelImp=new CodeModelImp(context);
    }


    public void register(String email ) {
        codeModelImp.loadCode(email,this);
    }


    public void unSubscribe() {
        codeModelImp.onUnsubscribe();
    }
}
