package com.jpble.activity;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jpble.R;
import com.jpble.base.BaseActivity;
import com.jpble.utils.Constant;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private final static int REQUECT_CODE_COARSE = 1;
    @BindView(R.id.main_logo)
    ImageView mainLogo;
    @BindView(R.id.main_ll)
    LinearLayout mainLl;
    Animation mHideAnimation;
    Animation mShowAnimation;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initPermission();
        setHideAnimation(2000);
        Log.e("getData", Constant.getData("FE6634253C4D7B60597F01044E"));

    }


    @OnClick({R.id.mian_sign, R.id.main_login, R.id.main_facebook, R.id.main_twitter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mian_sign:
                startActivity(new Intent(this, SignActivity.class));
                break;
            case R.id.main_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.main_facebook:
                break;
            case R.id.main_twitter:
                break;
            default:
                break;
        }
    }

    public void setHideAnimation(int duration) {
        if (duration < 0) {
            return;
        }

        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mainLogo.startAnimation(mHideAnimation);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainLogo.setVisibility(View.GONE);
                mainLl.setVisibility(View.VISIBLE);
                setShowAnimation(500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setShowAnimation(int duration) {
        if (duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mainLl.startAnimation(mShowAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
    }

    private void initPermission() {
        MPermissions.requestPermissions(this, REQUECT_CODE_COARSE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(REQUECT_CODE_COARSE)
    public void requestSdcardSuccess() {
    }

    @PermissionDenied(REQUECT_CODE_COARSE)
    public void requestSdcardFailed() {
        MPermissions.requestPermissions(this, REQUECT_CODE_COARSE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
