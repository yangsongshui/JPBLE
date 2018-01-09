package com.jpble.base;

/**
 * 描述：视图基类
 * 作者： on 2017/2/16 10:59
 */
public interface IBaseView<T> {
    /**
     * @descriptoin	请求前加载progress
     * @author	ys
     * @date 2017/6/13 11:00
     */
    void showProgress();

    /**
     * @descriptoin	请求结束之后隐藏progress
     * @author	ys
     * @date 2017/6/13 11:01
     */
    void disimissProgress();

    /**
     * @descriptoin	请求数据成功
     * @author	ys
     * @param tData 数据类型
     * @date 2017/6/13 11:01
     */
    void loadDataSuccess(T tData);

    /**
     * @descriptoin	请求数据错误
     * @author	ys
     * @param throwable 异常类型
     * @date 2017/6/13 11:01
     */
    void loadDataError(Throwable throwable);

}
