package com.jpble.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.ble.LinkBLE;
import com.jpble.utils.Constant;
import com.jpble.utils.SpUtils;
import com.jpble.utils.ToHex;

import butterknife.BindView;

import static com.jpble.utils.Constant.SECURITY_SWITCH;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_circle)
    ImageView homeCircle;
    @BindView(R.id.home_riding)
    CheckBox homeRiding;
    @BindView(R.id.home_lock)
    CheckBox homeLock;
    @BindView(R.id.home_max_speed)
    TextView homeMaxSpeed;
    @BindView(R.id.home_trip)
    TextView homeTrip;
    @BindView(R.id.home_time)
    TextView homeTime;

    LinkBLE linkBLE;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, Bundle savedInstanceState) {
        homeLock.setChecked(SpUtils.getBoolean(SECURITY_SWITCH, false));
        linkBLE = MyApplication.newInstance().getBleManager();
        Animation circle_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            homeCircle.startAnimation(circle_anim);  //开始动画
        }
        homeLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String key = MyApplication.newInstance().KEY + "120800" + (b ? "01" : "02") + "0000000000";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), key));
                SpUtils.putBoolean(SECURITY_SWITCH, b);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }


}
