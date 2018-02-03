package com.jpble.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpble.R;
import com.jpble.base.BaseActivity;
import com.jpble.utils.SpUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author omni20170501
 */
public class LoadingActivity extends BaseActivity {
    private final static int REQUECT_CODE_COARSE = 1;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.main_logo)
    ImageView mainLogo;
    Animation mHideAnimation;
    int[] imageID = {R.drawable.boot_page_1, R.drawable.boot_page_2, R.drawable.boot_page_3};
    int currentItem = 0;
    private List<View> mView = new ArrayList<View>();

    @Override
    protected int getContentView() {
        return R.layout.activity_loading;
    }


    @Override
    protected void init() {
        initPermission();
        if (SpUtils.getBoolean("one", true)) {
            pager.setVisibility(View.VISIBLE);
            mainLogo.setVisibility(View.GONE);
            initPager();
        } else {
            pager.setVisibility(View.GONE);
            mainLogo.setVisibility(View.VISIBLE);
            setHideAnimation(3000);
        }

        //sendNotification(DateUtil.getCurrDate(FORMAT_ONE), "Vibration was sensed with “My GEO”", "59");
    }


    //需要给ViewPager设置适配器
    PagerAdapter adapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        //有多少个切换页
        @Override
        public int getCount() {
            return imageID.length;
        }

        //对超出范围的资源进行销毁
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            //super.destroyItem(container, position, object);

            container.removeView(mView.get(position));
        }

        //对显示的资源进行初始化
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final View view = LayoutInflater.from(LoadingActivity.this).inflate(R.layout.load_item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.load_iv);
            TextView loadTv = (TextView) view.findViewById(R.id.load_skip);

            loadTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpUtils.putBoolean("one", false);
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    //进入主页
                    finish();

                }
            });
            Glide.with(LoadingActivity.this).load(imageID[position]).centerCrop().into(imageView);
            // imageView.setImageResource(imageID[position]);
            if (position == 2) {
                loadTv.setVisibility(View.GONE);
            } else {
                loadTv.setVisibility(View.VISIBLE);
            }
            mView.add(view);
            container.addView(view);
            return view;
        }

    };

    private void initPager() {
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;//获取位置，即第几页
                Log.i("Guide", "监听改变" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        pager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;
            float endX;
            float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        if (currentItem == (imageID.length - 1) && startX - endX >= (width / 4)) {
                            SpUtils.putBoolean("one", false);
                            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                            finish();//进入主页
                        }
                        break;
                }
                return false;
            }
        });
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
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();//进入主页
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }

    }


    private void initPermission() {
        MPermissions.requestPermissions(this, REQUECT_CODE_COARSE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
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
        MPermissions.requestPermissions(this, REQUECT_CODE_COARSE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void sendNotification(String title, String messageBody, String orderId) {
        Intent intent = new Intent(this, MapsActivity.class).putExtra("id",orderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(orderId) /* ID of notification */, notificationBuilder.build());
    }
}
