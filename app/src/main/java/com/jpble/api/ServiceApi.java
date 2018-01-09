package com.jpble.api;


import com.jpble.bean.Code;
import com.jpble.bean.Device;
import com.jpble.bean.User;

import java.util.Map;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：retrofit的接口service定义
 */
public interface ServiceApi {
    /**
     * 获取验证码
     */
    @POST("email/10001")
    Observable<Code> getCode(@Query("email") String email);

    /**
     * 注册
     */
    @POST
    Observable<Code> register(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 登陆
     */
    @POST("login/10001")
    Observable<User> login(@QueryMap Map<String, String> map);

    /**
     * 绑定设备
     */
    @POST
    Observable<Code> binding(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 删除设备
     */
    @DELETE
    Observable<Code> delete(@Url String url, @Query("lockId") String lockId, @Query("token") String token);

    /**
     * 获取设备列表
     */
    @GET("user/lock/10001")
    Observable<Device> getDevice(@Query("token") String token);
}
