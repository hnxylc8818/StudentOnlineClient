package com.stuonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

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
 * Created by SunJiShuang on 2015/9/15.
 */
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_title)
    private TitleView title;
    @ViewInject(R.id.login_account)
    private CustomerEditText etAccount;
    @ViewInject(R.id.login_pwd)
    private CustomerEditText etPwd;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        sp = getSharedPreferences("user_data", MODE_PRIVATE);
        String account = sp.getString("account", "");
        String pwd = sp.getString("pwd", "");
        //读取本地保存的用户信息并进行解密
        etAccount.setText(new String(Base64.decode(account, Base64.NO_WRAP)));
        etPwd.setText(new String(Base64.decode(pwd, Base64.NO_WRAP)));
    }

    @OnClick({R.id.login_login, R.id.login_reg, R.id.login_reset})
    public void onClick(View v) {
        Intent intent = new Intent(LoginActivity.this, ValidateActivity.class);
        switch (v.getId()) {
            case R.id.login_login:
                //登录按钮
                login();
                break;
            case R.id.login_reset:
                startActivity(intent);
                //找回密码
                break;
            case R.id.login_reg:
                startActivity(intent);
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
                    if (result.state == Result.STATE_SUC) {
                        MyApp.user = result.data;
                        String encodeAccount = Base64.encodeToString(MyApp.user.getAccount().getBytes(), Base64.NO_WRAP);
                        String encodePwd = Base64.encodeToString(MyApp.user.getPwd().getBytes(), Base64.NO_WRAP);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();//清除之前的数据
                        editor.putString("account", encodeAccount);
                        editor.putString("pwd", encodePwd);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, PersonalCenterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isNight= SharedUtil.getModel(this);
        if (isNight){
            setTheme(R.style.night);
        } else {
            setTheme(R.style.def);
        }
        init();
    }
}
