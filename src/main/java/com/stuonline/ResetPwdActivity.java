package com.stuonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
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

        String email = emailedt.getText().toString();
        if (email.isEmpty()) {
            XUtils.showToast("注册邮箱不能为空");
            return;
        } else if (!email.matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
            XUtils.showToast("邮箱格式错误");
            return;
        }
        dialog();
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
            startIntentAnim();
            dialog.dismiss();
        }
    };
}
