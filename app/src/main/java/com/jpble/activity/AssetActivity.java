package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Device;
import com.jpble.presenter.DevicePresenterImp;
import com.jpble.view.DeviceView;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetActivity extends BaseActivity implements DeviceView {


    @BindView(R.id.asset_rv)
    RelativeLayout assetRv;
    @BindView(R.id.asset_msg)
    TextView AssetMsg;
    DevicePresenterImp devicePresenterImp;
    ProgressDialog progressDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_asset;
    }

    @Override
    protected void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        devicePresenterImp=new DevicePresenterImp(this,this);
        devicePresenterImp.getDevice(MyApplication.newInstance().getUser().getData().getToken());
    }


    @OnClick({R.id.asset_delete, R.id.asset_add, R.id.asset_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.asset_delete:
                break;
            case R.id.asset_add:
                startActivity(new Intent(this,AddActivity.class));
                break;
            case R.id.asset_back:
                finish();
                break;
            default:
                break;
        }
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
    public void loadDataSuccess(Device tData) {

    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        devicePresenterImp.unSubscribe();
    }
}
