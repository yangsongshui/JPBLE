package com.jpble.api;


import com.jpble.bean.AddCode;
import com.jpble.bean.AddSim;
import com.jpble.bean.Code;
import com.jpble.bean.Device;
import com.jpble.bean.DeviceMsg;
import com.jpble.bean.Log;
import com.jpble.bean.LogInfo;
import com.jpble.bean.Sim;
import com.jpble.bean.User;
import com.jpble.ble.DeviceInfo;

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
    Observable<AddCode> binding(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 删除设备
     */
    @DELETE
    Observable<Code> delete(@Url String url, @Query("lockId") String lockId, @Query("token") String token);

    /**
     * 删除Sim
     */
    @DELETE
    Observable<Code> deleteSim(@Url String url, @Query("cardId") String cardId, @Query("token") String token);

    /**
     * 获取设备列表
     */
    @GET("user/lock/10001")
    Observable<Device> getDevice(@Query("token") String token);

    /**
     * 获取骑行历史数据
     */
    @GET("user/ride/10001")
    Observable<DeviceInfo> getRide(@Query("token") String token);

    /**
     * 开始骑行
     */
    @POST
    Observable<Code> startRide(@Url String url, @Query("token") String token, @Query("lockId") String lockId);

    /**
     * 结束骑行
     */
    @POST
    Observable<Code> endRide(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 追踪开关
     */
    @POST
    Observable<Code> tracking(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 追踪开关
     */
    @POST
    Observable<Code> antiTheft(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 添加用户SIM卡
     */
    @POST("user/simcard/10002")
    Observable<AddSim> addSim(@QueryMap Map<String, String> map);

    /**
     * 修改用户SIM卡
     */
    @POST
    Observable<Code> upSim(@Url String url,@QueryMap Map<String, String> map);

    /**
     * 获取用户SIM卡列表
     */
    @GET("user/simcard/10001")
    Observable<Sim> getSim(@Query("token") String token);

    /**
     * 	获取锁报警记录
     */
    @GET("user/lock/10008")
    Observable<LogInfo> getLog(@Query("token") String token);

    @GET
    Observable<Log> getLogInfo(@Url String url, @Query("orderId") String orderId, @Query("token") String token);

    @GET
    Observable<DeviceMsg> getDeviceInfo(@Url String url, @Query("lockId") String lockId, @Query("token") String token);
}
