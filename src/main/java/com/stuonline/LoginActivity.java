package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.stuonline.views.CustomerEditText;
import com.stuonline.views.TitleView;

/**
 * Created by SunJiShuang on 2015/9/15.
 */
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_title)
    private TitleView title;
    @ViewInject(R.id.login_account)
    private CustomerEditText etAccount;
    @ViewInject(R.id.login_pwd)
    private CustomerEditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.login_login, R.id.login_reg, R.id.login_reset})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                //登录按钮
                login();
                break;
            case R.id.login_reset:
                //找回密码
                break;
            case R.id.login_reg:
                //注册账号
                break;
        }
    }

    /**
     * 登录方法
     */
    private void login() {
        String account = etAccount.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (account.length() != 11) {
            XUtils.showToast("账号格式错误，请重新输入");
            return;
        }
        if (!pwd.matches("^\\w{6,20}$")) {
            XUtils.showToast("密码格式错误，请重新输入");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.account", account);
        params.addBodyParameter("u.pwd", pwd);
        DialogUtil.showWaitting(this);
        XUtils.send(XUtils.LOGIN, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DialogUtil.hiddenWaitting();
                if (responseInfo != null) {
                    JsonUtil<Result<Muser>> jsonUtil = new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>() {
                    });
                    Result<Muser> result = jsonUtil.parse(responseInfo.result);
                    XUtils.showToast(result.desc);
                    if (result.state==Result.STATE_SUC){
                        MyApp.user=result.data;
                        Intent intent = new Intent(LoginActivity.this,PersonalCenterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
