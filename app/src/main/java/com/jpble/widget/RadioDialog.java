package com.jpble.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jpble.R;

/**
 * Created by ys on 2017/9/7.
 */

public class RadioDialog extends Dialog {
    RadioButton radioA, radioB, radioC;
    RadioGroup radioRG;
    TextView radioTitle;
    View.OnClickListener onClickListener;

    public RadioDialog(@NonNull Context context) {
        super(context);
    }

    public RadioDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected RadioDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_radio);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        radioA = (RadioButton) findViewById(R.id.radio_a);
        radioB = (RadioButton) findViewById(R.id.radio_b);
        radioC = (RadioButton) findViewById(R.id.radio_c);
        radioTitle = (TextView) findViewById(R.id.radio_title);
        radioRG = (RadioGroup) findViewById(R.id.radio_rg);

        findViewById(R.id.radio_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.radio_confirm).setOnClickListener(onClickListener);
    }

    public void init(String id) {

        if (id.equals("01")) {
           radioRG.check(R.id.radio_a);
        }else if (id.equals("02")){
            radioRG.check(R.id.radio_b);
        }else if (id.equals("03")){
            radioRG.check(R.id.radio_c);
        }


    }

    public int getCheck() {
        return radioRG.getCheckedRadioButtonId();
    }
}
