package com.jpble.model;

import android.content.Context;

import com.jpble.api.ServiceApi;
import com.jpble.base.BaseModel;
import com.jpble.base.IBaseRequestCallBack;
import com.jpble.bean.AddSim;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ys on 2017/6/13.
 */

public class AddSimModelImp extends BaseModel{

    private Context context = null;
    private ServiceApi serviceApi;
    private CompositeSubscription mCompositeSubscription;

    public AddSimModelImp(Context mContext) {
        super();
        context = mContext;
        serviceApi = retrofitManager.getService();
        mCompositeSubscription = new CompositeSubscription();
    }

    public void loadCode( Map<String,String> map , final IBaseRequestCallBack<AddSim> iBaseRequestCallBack) {
        mCompositeSubscription.add(serviceApi.addSim(map)
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())   //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<AddSim>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onCompleted() {
                        //回调接口：请求已完成，可以隐藏progress
                        iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //回调接口：请求异常
                        iBaseRequestCallBack.requestError(e);
                    }

                    @Override
                    public void onNext(AddSim msg) {
                        iBaseRequestCallBack.requestSuccess(msg);

                    }

                })
        );
    }

    public void onUnsubscribe() {
        //判断状态
        if (mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.clear();  //注销
            mCompositeSubscription.unsubscribe();
        }
    }
}
