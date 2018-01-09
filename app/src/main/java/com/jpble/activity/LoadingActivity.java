package com.jpble.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpble.R;
import com.jpble.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author omni20170501
 */
public class LoadingActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    int[] imageID = {R.drawable.boot_page_1, R.drawable.boot_page_2, R.drawable.boot_page_3};
    int currentItem = 0;
    private List<View> mView = new ArrayList<View>();

    @Override
    protected int getContentView() {
        return R.layout.activity_loading;
    }


    @Override
    protected void init() {
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
                    pager.setCurrentItem(position + 1);
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


}
