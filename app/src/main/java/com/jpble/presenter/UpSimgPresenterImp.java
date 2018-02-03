package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.UpSimModelImp;
import com.jpble.view.CodeView;

import java.util.Map;

/**
 * 作者：omni20170501
 */

public class UpSimgPresenterImp extends BasePresenterImp<CodeView,Code> {


    private Context context = null;
    private UpSimModelImp upSimModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public UpSimgPresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        upSimModelImp=new UpSimModelImp(context);
    }

    public void register(String url, Map<String, String> map) {
        upSimModelImp.loadCode(url,map,this);
    }


    public void unSubscribe() {
        upSimModelImp.onUnsubscribe();
    }
}
