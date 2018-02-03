package com.jpble.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jpble.R;
import com.jpble.base.BaseFragment;

import butterknife.BindView;

/**
 * 作者：omni20170501
 */

public class WebFragment extends BaseFragment {
    @BindView(R.id.web)
    WebView web;


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WebSettings webSettings = web.getSettings();
        //方式1. 加载一个网页：
        web.loadUrl("http://eng-geo.crops-sports.com/contact/index");

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportZoom(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_web;
    }




}
