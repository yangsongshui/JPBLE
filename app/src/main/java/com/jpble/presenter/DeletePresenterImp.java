package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.DeleteModelImp;
import com.jpble.view.CodeView;

/**
 * 作者：omni20170501
 */

public class DeletePresenterImp extends BasePresenterImp<CodeView,Code> {


    private Context context = null;
    private DeleteModelImp deleteModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public DeletePresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        deleteModelImp=new DeleteModelImp(context);
    }


    public void register(String url, String lockId, String token) {
        deleteModelImp.loadCode(url,lockId,token,this);
    }


    public void unSubscribe() {
        deleteModelImp.onUnsubscribe();
    }
}
