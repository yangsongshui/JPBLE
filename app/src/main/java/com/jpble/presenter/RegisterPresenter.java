package com.jpble.presenter;

import java.util.Map;

/**
 * Created by ys on 2017/6/13.
 */

public interface RegisterPresenter {

    /**
     * @descriptoin	登陆接口
     * @author	ys
     * @param url 请求连接
     * @param map 注册参数
     * @date 2017/6/13
     * @return
     */
    void register(String url, Map<String ,String> map);

    /**
     * @descriptoin	注销subscribe
     * @author	ys
     * @date 2017/6/13
     */
    void unSubscribe();
}
