package com.stuonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.views.TitleView;

/**
 * Created by SunJiShuang on 2015/9/17.
 * 找回密码
 */
public class ResetPwdActivity extends BaseActivity {
    private Dialog dialog;
    @ViewInject(R.id.reset_pwd_title)
    private TitleView titleView;

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
            dialog.dismiss();
        }
    };
}
