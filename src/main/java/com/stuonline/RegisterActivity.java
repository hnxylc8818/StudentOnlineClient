package com.stuonline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.CustomerEditText;
import com.stuonline.views.TitleView;

/**
 * 孙卫星：注册Activity
 * Created by Administrator on 2015/9/15.
 */
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.register_title)
    private TitleView mTitle;
    @ViewInject(R.id.register_email)
    private CustomerEditText mEmail;
    @ViewInject(R.id.register_pwd)
    private CustomerEditText mPassword;
    @ViewInject(R.id.register_repeatepwd)
    private CustomerEditText mRepeatPassword;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        account = getIntent().getStringExtra("account");
    }

    @OnClick({R.id.title_left, R.id.title_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                //返回上一个页面
                finish();
                endIntentAnim();
                break;
            case R.id.title_right:
                //注册
                getRegisterEditText();
                break;
        }


    }

    /**
     * 获取注册页面的内容
     */
    public void getRegisterEditText() {
        String email = mEmail.getText();
        String password = mPassword.getText();
        String repeatPassword = mRepeatPassword.getText();
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(repeatPassword)) {
            if (TextUtils.isEmpty(email))
                XUtils.showToast("邮箱不能为空!");
            if (TextUtils.isEmpty(password))
                XUtils.showToast("密码不能为空!");
            if (TextUtils.isEmpty(repeatPassword))
                XUtils.showToast("重复密码不能为空!");
        } else {
            if (!email.matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
                XUtils.showToast("邮箱不正确！");
            } else {
                if (!password.equals(repeatPassword)) {
                    XUtils.showToast("密码不一致！");
                } else {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("u.account", account);
                    params.addBodyParameter("u.pwd", password);
                    params.addBodyParameter("u.email", email);
                    DialogUtil.showWaitting(this);
                    XUtils.send(XUtils.REG, params, new MyCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            DialogUtil.hiddenWaitting();
                            if (responseInfo != null) {
                                JsonUtil<Result<Boolean>> jsonUtil = new JsonUtil<Result<Boolean>>(new TypeReference<Result<Boolean>>() {
                                });
                                Result<Boolean> result = jsonUtil.parse(responseInfo.result);
                                XUtils.showToast(result.desc);
                                if (result.state == Result.STATE_SUC) {
                                    finish();
                                    endIntentAnim();
                                }

                            }
                        }
                    });
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

}
