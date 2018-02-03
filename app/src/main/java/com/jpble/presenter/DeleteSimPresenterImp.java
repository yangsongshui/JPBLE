package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.Code;
import com.jpble.model.DeleteSimModelImp;
import com.jpble.view.CodeView;

/**
 * 作者：omni20170501
 */

public class DeleteSimPresenterImp extends BasePresenterImp<CodeView,Code> {


    private Context context = null;
    private DeleteSimModelImp deleteSimModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public DeleteSimPresenterImp(CodeView view, Context context) {
        super(view);
        this.context=context;
        deleteSimModelImp=new DeleteSimModelImp(context);
    }


    public void register(String url, String cardId, String token) {
        deleteSimModelImp.loadCode(url,cardId,token,this);
    }


    public void unSubscribe() {
        deleteSimModelImp.onUnsubscribe();
    }
}
