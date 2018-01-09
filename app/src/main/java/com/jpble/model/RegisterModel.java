package com.jpble.model;


import com.jpble.base.IBaseRequestCallBack;

import java.util.Map;

/**
 * 描述：MVP中的M；登陆
 */
public interface RegisterModel<T> {

    /**
     * @descriptoin	获取网络数据
     * @author	Ys
     * @param url 请求连接
     * @param map 注册信息
     * @param iBaseRequestCallBack 数据的回调接口
     * @date 2017/2/17 19:01
     */
    void loadRegister(String url, Map<String ,String> map,IBaseRequestCallBack<T> iBaseRequestCallBack);

    /**
     * @descriptoin	注销subscribe
     * @author Ys
     * @param
     * @date 2017/2/17 19:02
     * @return
     */
    void onUnsubscribe();
}
