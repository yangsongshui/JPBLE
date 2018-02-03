package com.jpble.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jpble.OnItemListener;
import com.jpble.R;
import com.jpble.adapter.AssetAdapter;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Sim;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAssetActivity extends BaseActivity implements OnItemListener {

    @BindView(R.id.my_asset_rv)
    RecyclerView myAssetRv;
    AssetAdapter adapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_my_asset;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAssetRv.setLayoutManager(layoutManager);
        adapter=new AssetAdapter(this);
        myAssetRv.setAdapter(adapter);
        adapter.setOnItemCheckListener(this);
       // adapter.setDevice(new Device("设备1",false,true));
        //adapter.setDevice(new Device("设备2",true,true));
        //adapter.setDevice(new Device("设备3",true,false));

    }

    @OnClick(R.id.asset_back)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, Sim.DataBean bleDevice) {

    }
}
