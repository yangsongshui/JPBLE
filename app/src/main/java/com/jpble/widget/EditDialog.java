package com.jpble.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jpble.R;


/**
 * Created by ys on 2017/9/6.
 */

public class EditDialog extends Dialog implements View.OnClickListener, TextWatcher {
    TextView ed_title, ed_msg, ed_cancel, ed_confirm;
    EditText ed;
    View ed_re;
    View.OnClickListener onClickListener;
    private Context mContext;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public EditDialog(@NonNull Context context) {
        super(context);
    }

    public EditDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected EditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ed);
        setCanceledOnTouchOutside(true);
        initView();
    }


    private void initView() {
        ed_title = (TextView) findViewById(R.id.ed_title);
        ed_msg = (TextView) findViewById(R.id.ed_msg);
        ed_cancel = (TextView) findViewById(R.id.ed_cancel);
        ed_confirm = (TextView) findViewById(R.id.ed_confirm);
        ed = (EditText) findViewById(R.id.ed);
        findViewById(R.id.ed_re).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setText("");
            }
        });
        findViewById(R.id.ed_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ed_confirm.setOnClickListener(this);
        ed.addTextChangedListener(this);
        ed_confirm.setEnabled(false);
        ed_confirm.setTextColor(Color.rgb(197,199,202));
    }

    public void init(String title, String msg) {
        ed_title.setText(title);
        ed_msg.setText(msg);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public String getMsg() {
        return ed.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String msg = ed.getText().toString().trim();
        if (msg.length() > 0) {
            ed_confirm.setEnabled(true);
            ed_confirm.setTextColor(Color.rgb(16,16,17));
        } else {
            ed_confirm.setEnabled(false);
            ed_confirm.setTextColor(Color.rgb(197,199,202));
        }
    }
}
