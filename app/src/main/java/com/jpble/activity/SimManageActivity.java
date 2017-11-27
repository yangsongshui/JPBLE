package com.jpble.activity;

import android.os.Bundle;
import android.view.View;

import com.jpble.R;
import com.jpble.base.BaseActivity;
import com.jpble.widget.EditDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jpble.R.style.dialog;

public class SimManageActivity extends BaseActivity {
    EditDialog editDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_sim_manage;
    }

    @Override
    protected void init() {
        editDialog = new EditDialog(this, dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.sim_return, R.id.sim_set, R.id.sim_expiration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sim_return:
                break;
            case R.id.sim_set:
                editDialog.show();
                editDialog.init(getResources().getString(R.string.sim_set), getResources().getString(R.string.sim_set2));
                break;
            case R.id.sim_expiration:
                break;
            default:
                break;
        }
    }
}
