package com.jpble.presenter;

import android.content.Context;

import com.jpble.base.BasePresenterImp;
import com.jpble.bean.User;
import com.jpble.model.LoginModelImp;
import com.jpble.view.LoginView;

import java.util.Map;


/**
 * 描述：MVP中的P实现类
 */
public class LoginPresenterImp extends BasePresenterImp<LoginView,User> implements LoginPresenter{
    //传入泛型V和T分别为WeatherView、WeatherInfoBean表示建立这两者之间的桥梁
    private Context context = null;
    private LoginModelImp loginModelImp = null;
    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     * @author ys
     * @date 2017/6/13 15:12
     */
    public LoginPresenterImp(LoginView view, Context context) {
        super(view);
        this.context = context;
        this.loginModelImp = new LoginModelImp(context);
    }

    @Override
    public void loadLogin(Map<String, String> map) {
        loginModelImp.loadLogin(map, this);
    }

    @Override
    public void unSubscribe() {
        loginModelImp.onUnsubscribe();
    }
}
