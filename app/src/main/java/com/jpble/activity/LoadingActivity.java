package com.jpble.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author omni20170501
 */
public class LoadingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.pager)
    ViewPager pager;
    int[] imageID = {R.drawable.boot_page_1, R.drawable.boot_page_2, R.drawable.boot_page_3, R.drawable.boot_page_4};
    private List<View> mView = new ArrayList<View>();

    @Override
    protected int getContentView() {
        return R.layout.activity_loading;
    }

    @Override
    protected void init() {

        pager.setAdapter(adapter);

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
            TextView loadBt = (TextView) view.findViewById(R.id.load_bt);
            loadTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(position + 1);
                }
            });
            loadBt.setOnClickListener(LoadingActivity.this);
            imageView.setImageResource(imageID[position]);
            if (position == 3) {
                loadBt.setVisibility(View.VISIBLE);
                loadTv.setVisibility(View.GONE);
            } else {
                loadBt.setVisibility(View.GONE);
                loadTv.setVisibility(View.VISIBLE);
            }
            mView.add(view);
            container.addView(view);
            return view;
        }

    };

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }


}
