package com.jpble.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jpble.R;


/**
 * Created by ys on 2017/9/1.
 */

public class CommomDialog extends Dialog implements View.OnClickListener {

    private TextView titleTxt;
    private TextView cancelTxt;
    private EditText dialog_psw;
    View.OnClickListener onClickListener;
    private Context mContext;

    public CommomDialog(@NonNull Context context) {
        super(context);
    }

    public CommomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CommomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        cancelTxt = (TextView) findViewById(R.id.cancel);
        dialog_psw = (EditText) findViewById(R.id.dialog_psw);
        cancelTxt.setOnClickListener(this);


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
    public String getMsg(){
        String msg=dialog_psw.getText().toString().trim();
        dialog_psw.setText("");
        return msg;
    }

}
