package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jpble.OnItemListener;
import com.jpble.R;
import com.jpble.adapter.MyGeoAdapter;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Code;
import com.jpble.bean.Sim;
import com.jpble.presenter.DeletePresenterImp;
import com.jpble.presenter.DeleteSimPresenterImp;
import com.jpble.presenter.SimPresenterImp;
import com.jpble.utils.AESUtil;
import com.jpble.view.CodeView;
import com.jpble.view.SimView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.AESUtil.PHONE_KEY;
import static com.jpble.utils.Constant.BASE_URL;

public class AssetActivity extends BaseActivity implements SimView, OnItemListener {


    @BindView(R.id.asset_rv)
    RecyclerView assetRv;
    @BindView(R.id.asset_msg)
    TextView AssetMsg;
    SimPresenterImp simPresenterImp;
    ProgressDialog progressDialog;
    MyGeoAdapter adapter;
    DeletePresenterImp deletePresenterImp;
    DeleteSimPresenterImp deleteSimPresenterImp;

    @Override
    protected int getContentView() {
        return R.layout.activity_asset;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assetRv.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        deletePresenterImp = new DeletePresenterImp(new CodeView() {

            @Override
            public void showProgress() {
                progressDialog.show();
            }

            @Override
            public void disimissProgress() {

            }


            @Override
            public void loadDataSuccess(Code tData) {
                if (tData.getCode() == 200)
                    deleteSim();
                else
                    err(tData.getCode());
            }

            @Override
            public void loadDataError(Throwable throwable) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.e("loadDataError", throwable.getMessage());
                showToastor(getString(R.string.login_msg10));
            }
        }, this);
        deleteSimPresenterImp = new DeleteSimPresenterImp(new CodeView() {
            @Override
            public void showProgress() {

            }

            @Override
            public void disimissProgress() {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void loadDataSuccess(Code tData) {
                if (tData.getCode() == 200)
                    simPresenterImp.getDevice(MyApplication.newInstance().getUser().getData().getToken());
                else
                    err(tData.getCode());
            }

            @Override
            public void loadDataError(Throwable throwable) {
                showToastor(getString(R.string.login_msg10));
            }
        }, this);

        adapter = new MyGeoAdapter(this);
        assetRv.setAdapter(adapter);
        adapter.setOnItemCheckListener(this);
        simPresenterImp = new SimPresenterImp(this, this);
    }


    @OnClick({R.id.asset_delete, R.id.asset_add, R.id.asset_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.asset_delete:
                delete();
                break;
            case R.id.asset_add:
                startActivity(new Intent(this, AddActivity.class));

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
    public void loadDataSuccess(Sim tData) {
        if (tData.getCode() == 200) {
            isMac(tData.getData());
            adapter.setItems(tData.getData());
        } else
            err(tData.getCode());
    }

    public void isMac(List<Sim.DataBean> data) {
        boolean is = false;
        if (MyApplication.newInstance().bindMac != null && !MyApplication.newInstance().bindMac.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                if (MyApplication.newInstance().bindMac.equals(data.get(i).getLockVo().getMac())) {
                    is = true;
                }
            }
            if (!is) {
                MyApplication.newInstance().getBleManager().closeBle();
            }
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simPresenterImp.unSubscribe();
        deletePresenterImp.unSubscribe();
        deleteSimPresenterImp.unSubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.newInstance().getUser() != null)
            simPresenterImp.getDevice(MyApplication.newInstance().getUser().getData().getToken());
    }

    private void delete() {
        String id = "";
        Map<Integer, Boolean> map = adapter.getMap();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                id = adapter.getDevice(i).getLockId() + "," + id;
            }
        }
        if (!id.isEmpty()) {
            id = id.substring(0, id.length() - 1);
            Log.e("id", id);
            String token = MyApplication.newInstance().getUser().getData().getToken();
            String url = BASE_URL + "user/lock/10003/" + id;
            deletePresenterImp.register(url, id, token);
        }

    }

    private void deleteSim() {
        String id = "";
        Map<Integer, Boolean> map = adapter.getMap();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                id = adapter.getDevice(i).getId() + "," + id;
            }
        }
        id = id.substring(0, id.length() - 1);
        Log.e("id", id);
        String token = MyApplication.newInstance().getUser().getData().getToken();
        String url = BASE_URL + "user/simcard/10005/" + id;
        deleteSimPresenterImp.register(url, id, token);
    }

    @Override
    public void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, Sim.DataBean bleDevice) {
        String msg = "APN:" + bleDevice.getApn() + ",USER:" + bleDevice.getAccount() + ",USER_PWD:" + AESUtil.aesDecrypt(bleDevice.getPassword(), PHONE_KEY) + ",ICCID:" + bleDevice.getNumber() + ",";
        startActivity(new Intent(this, AssetInfoActivity.class)
                .putExtra("lockId", String.valueOf(bleDevice.getLockId()))
                .putExtra("psw", AESUtil.aesDecrypt(bleDevice.getLockVo().getPassword(), PHONE_KEY))
                .putExtra("id", String.valueOf(bleDevice.getId())).putExtra("type", bleDevice.getAuthType())
                .putExtra("name", bleDevice.getLockVo().getName()).putExtra("mac", bleDevice.getLockVo().getMac()).putExtra("msg", msg));
    }
}
