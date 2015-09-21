package com.stuonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

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
 * Created by SunJiShuang on 2015/9/17.
 * 验证邮箱找回密码(验证页)
 */
public class ResetPwdActivity extends BaseActivity {
    private Dialog dialog;
    @ViewInject(R.id.reset_pwd_title)
    private TitleView titleView;
    @ViewInject(R.id.reset_pwd_email)
    private CustomerEditText emailedt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        setContentView(R.layout.activity_reset_pwd);
        ViewUtils.inject(this);
        titleView.setOnLeftclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                endIntentAnim();
            }
        });

    }

    @OnClick(R.id.reset_pwd_bt)
    public void onClick(View v) {

        final String email = emailedt.getText().toString();
        if (email.isEmpty()) {
            XUtils.showToast("注册邮箱不能为空");
            return;
        } else if (!email.matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
            XUtils.showToast("邮箱格式错误");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("mailAddr", email);
        DialogUtil.showWaitting(this);
        httpHandler=XUtils.send(XUtils.SMAIL, params, new MyCallBack<String>() {
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
                        dialog();

                    }
                }
            }
        });

    }

    private void dialog() {
        if (dialog == null) {
            View v = LayoutInflater.from(ResetPwdActivity.this).inflate(
                    R.layout.layout_repwd_dialog, null);
            v.setBackgroundColor(Color.WHITE);
            Button button = (Button) v.findViewById(R.id.select_dialog_helper_bt);
            button.setOnClickListener(onClickListener);
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ResetPwdActivity.this);
            dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
            Window w = dialog.getWindow();
            w.setContentView(v);
            w.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        } else {
            dialog.show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ResetPwdActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
            finish();
            startIntentAnim();
            dialog.dismiss();
        }
    };
}
