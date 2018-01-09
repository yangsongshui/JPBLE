package com.jpble.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.jpble.R;


/**
 * Created by ys on 2017/9/1.
 */

public class HintDialog extends Dialog implements View.OnClickListener {


    private TextView hintConfirm,hintCancel;
    View.OnClickListener onClickListener;
    private Context mContext;

    public HintDialog(@NonNull Context context) {
        super(context);
    }

    public HintDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected HintDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_hint);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        hintConfirm = (TextView) findViewById(R.id.hint_confirm);
        hintCancel = (TextView) findViewById(R.id.hint_cancel);
        hintCancel.setOnClickListener(this);
        hintConfirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
        this.dismiss();
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }



}
