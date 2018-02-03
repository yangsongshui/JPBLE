package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.AddSim;
import com.jpble.model.AddSimModelImp;
import com.jpble.view.AddSimView;

import java.util.Map;

/**
 * 作者：omni20170501
 */

public class AddSimgPresenterImp extends BasePresenterImp<AddSimView,AddSim> {


    private Context context = null;
    private AddSimModelImp addSimModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public AddSimgPresenterImp(AddSimView view, Context context) {
        super(view);
        this.context=context;
        addSimModelImp=new AddSimModelImp(context);
    }

    public void register( Map<String, String> map) {
        addSimModelImp.loadCode(map,this);
    }


    public void unSubscribe() {
        addSimModelImp.onUnsubscribe();
    }
}
