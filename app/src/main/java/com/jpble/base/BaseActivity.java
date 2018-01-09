package com.jpble.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.jpble.R;
import com.jpble.utils.Toastor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;


public abstract class BaseActivity extends FragmentActivity {

    Toastor toastor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentView());
        ButterKnife.bind(this);
        //用于显示当前位于哪个活动
        Log.d("BaseActivity", getClass().getSimpleName());
        init();
        toastor=new Toastor(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //注入布局
    protected abstract int getContentView();

    //初始化
    protected abstract void init();
    public void showToastor(String msg){
        toastor.showSingletonToast(msg);
    }

    public void err(int code) {
        switch (code) {
            case 101:
                showToastor(getString(R.string.login_msg12));
                break;
            case 102:
                showToastor(getString(R.string.login_msg13));
                break;
            case 103:
                showToastor(getString(R.string.login_msg14));
                break;
            case 104:
                showToastor(getString(R.string.login_msg15));
                break;
            case 201:
                showToastor(getString(R.string.login_msg16));
                break;
            case 202:
                showToastor(getString(R.string.login_msg17));
                break;
            case 203:
                showToastor(getString(R.string.login_msg18));
                break;
            case 204:
                showToastor(getString(R.string.login_msg19));
                break;
            case 205:
                showToastor(getString(R.string.login_msg20));
                break;
            default:
                showToastor(getString(R.string.login_msg21));
                break;
        }

    }
    /***
     * 判断email格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
