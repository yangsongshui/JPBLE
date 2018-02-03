package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jpble.OnLogItemListener;
import com.jpble.R;
import com.jpble.adapter.LogAdapter;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.LogInfo;
import com.jpble.presenter.GetLogPresenterImp;
import com.jpble.view.LogInfoView;

import butterknife.BindView;
import butterknife.OnClick;

public class LogActivity extends BaseActivity implements LogInfoView, OnLogItemListener {


    @BindView(R.id.log_rv)
    RecyclerView logRv;
    LogAdapter adapter;
    GetLogPresenterImp getLogPresenterImp;
    ProgressDialog progressDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_log;
    }

    @Override
    protected void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        logRv.setLayoutManager(layoutManager);
        adapter = new LogAdapter(this);
        getLogPresenterImp = new GetLogPresenterImp(this, this);
        getLogPresenterImp.register(MyApplication.newInstance().getUser().getData().getToken());
        logRv.setAdapter(adapter);
        adapter.setOnItemCheckListener(this);
    }


    @OnClick(R.id.log_return)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void disimissProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(LogInfo tData) {
    adapter.setItems(tData.getData());
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

    @Override
    public void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, LogInfo.DataBean bleDevice) {

        startActivity(new Intent(this,MapsActivity.class).putExtra("lng",String.valueOf(bleDevice.getLockVo().getGLng())).putExtra("lat",String.valueOf(bleDevice.getLockVo().getGLat())));
    }
}
