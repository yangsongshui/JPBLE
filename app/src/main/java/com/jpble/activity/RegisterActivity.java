package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Code;
import com.jpble.presenter.CodePresenterImp;
import com.jpble.utils.AESUtil;
import com.jpble.view.CodeView;
import com.jpble.widget.AutoLinkStyleTextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.AESUtil.PHONE_KEY;

public class RegisterActivity extends BaseActivity implements CodeView {


    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.register_mail)
    EditText registerMail;
    @BindView(R.id.register_psw)
    EditText registerPsw;
    @BindView(R.id.register_psw2)
    EditText registerPsw2;
    @BindView(R.id.register_msg)
    AutoLinkStyleTextView registerMsg;
    CodePresenterImp codePresenterImp;
    ProgressDialog progressDialog;
    int type = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type", 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        registerMsg.setOnClickCallBack(new AutoLinkStyleTextView.ClickCallBack() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url;
                if (position == 1) {
                    content_url = Uri.parse("http://geo.crops-sports.com/terms-of-service/");
                } else {
                    content_url = Uri.parse("http://geo.crops-sports.com/privacy-policy/");
                }
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        codePresenterImp = new CodePresenterImp(this, this);
    }


    @OnClick({R.id.mian_send, R.id.register_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mian_send:
                String psw = registerPsw.getText().toString().trim();
                String psw2 = registerPsw2.getText().toString().trim();
                String mail = registerMail.getText().toString().trim();
                if (psw.equals(psw2) && psw.length() >= 6) {

                    if (isEmail(mail)) {
                        codePresenterImp.register(AESUtil.aesEncrypt(mail, PHONE_KEY));
                    } else {
                        showToastor(getString(R.string.register_tos2));
                    }
                } else {
                    showToastor(getString(R.string.register_tos));
                }

                break;
            case R.id.register_back:
                finish();
                break;
            default:
                break;
        }
    }

    /***
     * 判断email格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        codePresenterImp.unSubscribe();
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
    public void loadDataSuccess(Code tData) {
        Log.e("loadDataSuccess", tData.toString());
        if (tData.getCode() == 200 && tData.getData() == 1) {
            startActivity(new Intent(this, CodeActivity.class).putExtra("type", type).putExtra("psw", registerPsw.getText().toString()).putExtra("mail", registerMail.getText().toString()));
            finish();
        } else {
            err(tData.getCode());
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        showToastor(getString(R.string.login_msg10));
    }

    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }
}
