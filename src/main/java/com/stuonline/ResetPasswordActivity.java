package com.stuonline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
import com.stuonline.views.CustomerEditText;
import com.stuonline.views.TitleView;

/**
 * Created by Administrator on 2015/9/17.
 */
public class ResetPasswordActivity extends BaseActivity {
    @ViewInject(R.id.verify_title)
    private TitleView verfytitle;
    @ViewInject(R.id.verify_email)
    private CustomerEditText verifytv;
    @ViewInject(R.id.verify_pwd)
    private CustomerEditText verifypwd;
    @ViewInject(R.id.verify_repeatepwd)
    private CustomerEditText verifyrepstpwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_verify);
        ViewUtils.inject(this);

    }
    @OnClick({R.id.title_left,R.id.title_right,})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_left:
                finish();
                startIntentAnim();
                break;
            case R.id.title_right:

                break;
        }
    }
    public void updatepwd(){
        String Verify=verifytv.getText().toString();
        String pwd=verifypwd.getText().toString();
        String repeatPwd=verifyrepstpwd.getText().toString();
        if (TextUtils.isEmpty(Verify)){
            XUtils.showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            XUtils.showToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(repeatPwd)){
            XUtils.showToast("确认密码不能为空");
            return;
        }
        if (pwd.equals(repeatPwd)){
            XUtils.showToast("密码不一致!");
            return;
        }

    }
}
