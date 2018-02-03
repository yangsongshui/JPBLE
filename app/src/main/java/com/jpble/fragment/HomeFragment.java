package com.jpble.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.MapActivity;
import com.jpble.activity.TripActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.bean.Code;
import com.jpble.ble.LinkBLE;
import com.jpble.presenter.EndPresenterImp;
import com.jpble.presenter.StartPresenterImp;
import com.jpble.utils.Toastor;
import com.jpble.view.CodeView;
import com.jpble.widget.AMapUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.BASE_URL;
import static com.jpble.utils.Constant.DATA_MAP;
import static com.jpble.utils.Constant.END_RIDING;
import static com.jpble.utils.Constant.STARTED_RIDING;


public class HomeFragment extends BaseFragment implements CodeView {

    @BindView(R.id.home_circle)
    ImageView homeCircle;
    @BindView(R.id.home_riding)
    CheckBox homeRiding;
    @BindView(R.id.me_trip)
    TextView meTrip;
    @BindView(R.id.home_max_speed)
    TextView homeMaxSpeed;
    @BindView(R.id.home_trip)
    TextView homeTrip;
    @BindView(R.id.home_time)
    Chronometer homeTime;
    @BindView(R.id.home_speed)
    TextView homeSpeed;
    LinkBLE linkBLE;
    Toastor toastor;
    StartPresenterImp startPresenterImp;
    EndPresenterImp endPresenterImp;
    ProgressDialog progressDialog;
    Code code;
    Animation circle_anim;
    double trip = 0.0;
    double maxSpeed = 0.0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        toastor = new Toastor(getActivity());
        linkBLE = MyApplication.newInstance().getBleManager();
        startPresenterImp = new StartPresenterImp(this, getActivity());
        endPresenterImp = new EndPresenterImp(this, getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.login_msg7));
        circle_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();
        circle_anim.setInterpolator(interpolator);

        meTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TripActivity.class));
            }
        });
        homeRiding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (MyApplication.newInstance().scearch) {
                    if (isChecked) {
                        startRide();
                    } else {
                        endRide();
                    }
                } else {
                    toastor.showSingletonToast(getString(R.string.toastor_msg4));
                }

            }
        });
        homeTime.setFormat("00:%s");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DATA_MAP);
        getActivity().registerReceiver(notifyReceiver, intentFilter);
    }

    private void endRide() {
        Intent intent = new Intent();
        intent.setAction(END_RIDING);
        getActivity().sendBroadcast(intent);
        // MyApplication.newInstance().latLng.add(0, MyApplication.newInstance().startLng);
        String lng = AMapUtil.encode(MyApplication.newInstance().latLng);
        Log.e("定位坐标", "所有坐标：" + lng);
        if (code != null) {
            Map<String, String> map = new HashMap<>();
            String token = MyApplication.newInstance().getUser().getData().getToken();
            String url = BASE_URL + "user/ride/10003/" + code.getData();
            map.put("rideDate", code.getData() + "");
            map.put("token", token);
            //  Log.e("time", token);
            map.put("speed", homeSpeed.getText().toString());
            map.put("maxSpeed", homeMaxSpeed.getText().toString());
            map.put("orbit", lng);
            map.put("distance", homeTrip.getText().toString());
            endPresenterImp.register(url, map);
            homeCircle.clearAnimation();
        }
        homeTime.stop();
        homeTime.setBase(SystemClock.elapsedRealtime());
        trip = 0.0;
        maxSpeed = 0.0;
        homeCircle.clearAnimation();
        homeMaxSpeed.setText("0");
        homeSpeed.setText("0");
        MyApplication.newInstance().latLng.clear();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    private void startRide() {
        //获取本地ID
        String id = MyApplication.newInstance().id;
        String token = MyApplication.newInstance().getUser().getData().getToken();
        String url = BASE_URL + "user/ride/10002/" + id;
        startPresenterImp.register(url, token, id);
        if (circle_anim != null) {
            homeCircle.startAnimation(circle_anim);  //开始动画
        }
        Intent intent = new Intent();
        intent.setAction(STARTED_RIDING);
        getActivity().sendBroadcast(intent);
        homeTime.setBase(SystemClock.elapsedRealtime());
        homeTime.start();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void disimissProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void loadDataSuccess(Code tData) {
        code = tData;
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        //toastor.showSingletonToast(getString(R.string.login_msg10));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(notifyReceiver);

    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DATA_MAP.equals(intent.getAction())) {
                int distance = (int) intent.getFloatExtra("distance", 0);
                int sped = intent.getIntExtra("sped", 0);
                double total = (distance / 1000.00);
                double speed = (sped * 60 * 60) / 1000;
                BigDecimal b = new BigDecimal(total);
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (f1 > trip) {
                    BigDecimal b2 = new BigDecimal(speed);
                    speed = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    homeSpeed.setText(String.valueOf(speed));
                    if (speed > maxSpeed) {
                        homeMaxSpeed.setText(String.valueOf(speed));
                        maxSpeed = speed;
                    }
                    homeTrip.setText(String.valueOf(f1));
                    trip = f1;
                }

            }
        }
    };



    @OnClick(R.id.home_map)
    public void onViewClicked() {
        if (MyApplication.newInstance().latLng.size() > 0)
            startActivity(new Intent(getActivity(), MapActivity.class));
    }
}
