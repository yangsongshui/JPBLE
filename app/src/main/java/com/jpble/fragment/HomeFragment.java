package com.jpble.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.activity.TripActivity;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;

import butterknife.BindView;

import static com.jpble.utils.Constant.LOCK_STATUS;
import static com.jpble.utils.Constant.SECURITY_SWITCH;


public class HomeFragment extends BaseFragment {

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
    TextView homeTime;

    LinkBLE linkBLE;
    Toastor toastor;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        toastor = new Toastor(getActivity());
        linkBLE = MyApplication.newInstance().getBleManager();
        Animation circle_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            homeCircle.startAnimation(circle_anim);  //开始动画
        }
        meTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TripActivity.class));
            }
        });
        homeRiding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SpUtils.getBoolean(SECURITY_SWITCH, false)) {
                    toastor.showSingletonToast("自动模式，无需手动开关锁");
                }else {
                    String msg = MyApplication.newInstance().KEY + "1207" + "00" + (isChecked ? "01" : "02")+"000000";
                    linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                    SpUtils.putString(LOCK_STATUS, (isChecked ? "01" : "02"));

                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }


}
