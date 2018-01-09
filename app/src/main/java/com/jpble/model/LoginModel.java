package com.jpble.model;


import com.jpble.base.IBaseRequestCallBack;

import java.util.Map;

/**
 * 描述：MVP中的M；登陆
 */
public interface LoginModel<T> {

    /**
     * @descriptoin	获取网络数据
     * @author
     * @param iBaseRequestCallBack 数据的回调接口
     * @date 2017/2/17 19:01
     */
    void loadLogin(Map<String, String> map, IBaseRequestCallBack<T> iBaseRequestCallBack);

    /**
     * @descriptoin	注销subscribe
     * @author Ys
     * @param
     * @date 2017/2/17 19:02
     * @return
     */
    void onUnsubscribe();
}
