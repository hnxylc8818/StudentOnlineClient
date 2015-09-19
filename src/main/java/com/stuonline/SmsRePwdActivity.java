package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.JsonUtil;
import com.stuonline.views.CustomerEditText;
import com.stuonline.views.TitleView;

/**
 * Created by SunJiShuang on 2015/9/17.
 * 短信找回密码
 */
public class SmsRePwdActivity extends BaseActivity {
    @ViewInject(R.id.sms_repwd_title)
    private TitleView titleView;
    @ViewInject(R.id.sms_repwd_newpwd)
    private CustomerEditText etNewPwd;
    @ViewInject(R.id.sms_repwd_repwd)
    private CustomerEditText etRePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        setContentView(R.layout.activity_sms_repwd);
        ViewUtils.inject(this);
        titleView.setOnRightTextclickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    finish();
                    endIntentAnim();
                    break;
                case R.id.title_right:
                    loadData();
                    break;
            }

        }
    };

    private void loadData() {
        String account = getIntent().getStringExtra("account");
        String newPwd = etNewPwd.getText().toString().trim();
        String rePwd = etRePwd.getText().toString().trim();
        if (!newPwd.matches("^\\w{6,20}$")) {
            XUtils.showToast("新密码格式错误，请重新输入");
            return;
        }
        if (!rePwd.equals(newPwd)) {
            XUtils.showToast("两次输入密码不一致，请重新输入");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.uid", String.valueOf(MyApp.user.getUid()));
        params.addBodyParameter("u.pwd", newPwd);
        XUtils.send(XUtils.UUSER, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                if (responseInfo != null) {
                    JsonUtil<Result<Muser>> jsonUtil = new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>() {
                    });
                    Result<Muser> result = jsonUtil.parse(responseInfo.result);
                    XUtils.showToast(result.desc);
                    if (result.state == Result.STATE_SUC) {
                        MyApp.user = result.data;
                        finish();
                        endIntentAnim();
                    }
                }
            }
        });
    }
}
