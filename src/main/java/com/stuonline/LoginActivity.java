package com.stuonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
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
import com.stuonline.thirds.demo.tpl.ThirdLoginActivity;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.EditDialog;
import com.stuonline.utils.JsonUtil;
import com.stuonline.views.CustomerEditText;
import com.stuonline.views.SelectPicPopupWindow;
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

    private SelectPicPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        title.setOnRightTextclickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, ThirdLoginActivity.class);
            startActivity(intent);
        }
    };

    @OnClick({R.id.login_login, R.id.login_reg, R.id.login_reset})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                //登录按钮
                login();
                break;
            case R.id.login_reset:
                //找回密码
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(LoginActivity.this, itemsOnClick, MyApp.RESET);
                //显示窗口
                menuWindow.showAtLocation(LoginActivity.this.findViewById(R.id.login_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.login_reg:
                MyApp.type = 1;
                Intent intent = new Intent(LoginActivity.this, ValidateActivity.class);
                startActivity(intent);
                startIntentAnim();
                //注册账号
                break;
        }
    }

    private Intent intent;

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_top:
                    // 跳转手机验证界面
                    MyApp.type = 2;
                    intent = new Intent(LoginActivity.this, VliPhoneActivity.class);
                    startActivity(intent);
                    startIntentAnim();
                    break;
                case R.id.btn_bottom:
                    // 跳转邮箱验证界面
                    intent = new Intent(LoginActivity.this, ResetPwdActivity.class);
                    intent.putExtra("account", etAccount.getText().toString());
                    startActivity(intent);
                    startIntentAnim();
                    break;
                default:
                    break;
            }
        }

    };

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
        httpHandler = XUtils.send(XUtils.LOGIN, params, new MyCallBack<String>() {
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
                        startIntentAnim();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
