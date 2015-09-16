package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.entity.Feedback;
import com.stuonline.utils.FeedbackDialog;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.TitleView;

/**
 * Created by SunJiShuang on 2015/9/15.
 */
public class PersonalCenterActivity extends BaseActivity {

    @ViewInject(R.id.personal_center_account)
    private TextView personAccount;
    @ViewInject(R.id.personal_center_nick)
    private TextView personNick;
    @ViewInject(R.id.personal_center_school)
    private TextView personSchool;
    @ViewInject(R.id.personal_center_title)
    private TitleView personTitle;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        setContentView(R.layout.activity_personal_center);
        ViewUtils.inject(this);
        if (null != MyApp.user) {
            personAccount.setText(String.format("账号：%s", MyApp.user.getAccount()));
            personNick.setText(String.format("昵称：%s", MyApp.user.getNick()));
            personSchool.setText(String.format("学校：%s", MyApp.user.getSchool()));
        }
    }

    @OnClick({R.id.personal_center_person_info, R.id.personal_center_school_msg, R.id.personal_center_safe,
            R.id.personal_center_setting, R.id.personal_center_feedback, R.id.personal_center_exit})
    private void click(View v) {
        switch (v.getId()) {
            case R.id.personal_center_person_info:
                // 跳转个人信息

                break;
            case R.id.personal_center_school_msg:
                // 跳转校内信息

                break;
            case R.id.personal_center_safe:
                // 跳转账号与安全

                break;
            case R.id.personal_center_setting:
                // 跳转系统设置
                intent = new Intent(PersonalCenterActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_center_feedback:
                // 跳转意见反馈
                FeedbackDialog.showWaitting(this);
                break;
            case R.id.personal_center_exit:
                // 退出登录，跳转登录页面,并注销当前账号
                MyApp.release();
                intent = new Intent(PersonalCenterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
        init();
    }
}
