package com.jpble.fragment;

import android.os.Bundle;
import android.view.View;

import com.jpble.R;
import com.jpble.base.BaseFragment;


public class MapFragment extends BaseFragment {

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, Bundle savedInstanceState) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_map;
    }
}
