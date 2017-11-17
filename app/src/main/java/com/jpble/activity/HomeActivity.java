package com.jpble.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.jpble.R;
import com.jpble.base.BaseFragment;
import com.jpble.fragment.HomeFragment;
import com.jpble.fragment.MapFragment;
import com.jpble.fragment.MeFragment;
import com.jpble.fragment.SecurityFragment;
import com.jpble.utils.Toastor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.home_rgrpNavigation)
    RadioGroup mainRgrpNavigation;

    private Fragment[] frags = new Fragment[4];
    protected BaseFragment currentFragment;
    private HomeFragment homeFragment;
    Toastor toastor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_home);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initData();
        mainRgrpNavigation.check(R.id.home_riding);
        mainRgrpNavigation.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.home_riding:
                showFragment(0);
                break;
            case R.id.home_trail:
                showFragment(1);
                break;
            case R.id.home_security:
                showFragment(2);
                break;
            case R.id.home_set:
                showFragment(3);
                break;
            default:
                break;
        }
    }
    private void initData() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }

        if (!homeFragment.isAdded()) {

            getSupportFragmentManager().beginTransaction().add(R.id.home_frame, homeFragment).commit();

            currentFragment = homeFragment;
        }

    }
    private void showFragment(int position) {
        if (frags[position] == null) {
            frags[position] = getFrag(position);
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(), frags[position]);
    }

    @Nullable
    private Fragment getFrag(int index) {
        switch (index) {
            case 0:
                return homeFragment;
            case 1:
                return new MapFragment();
            case 2:
                return new SecurityFragment();
            case 3:
                return new MeFragment();
            default:
                return null;
        }
    }
    /**
     * 添加或者显示 fragment
     *
     * @param transaction
     * @param fragment
     */
    protected void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }

        if (!fragment.isAdded()) {
            // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.home_frame, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = (BaseFragment) fragment;


    }
}
