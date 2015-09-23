package com.stuonline;

import android.os.Bundle;
import android.util.Log;
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
import com.stuonline.utils.JsonUtil;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.CustomerEditText;

/**
 * Created by SunJiShuang on 2015/9/16.
 */
public class UpdatePasswordActivity extends BaseActivity {
    @ViewInject(R.id.uppwd_oldpassword)
    private CustomerEditText etOldPwd;
    @ViewInject(R.id.uppwd_newpassword)
    private CustomerEditText etNewPwd;
    @ViewInject(R.id.uppwd_repeatpassword)
    private CustomerEditText etRePwd;


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
        setContentView(R.layout.activity_updatepassword);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.title_left, R.id.title_right})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                //返回上一页
                finish();
                endIntentAnim();
                break;
            case R.id.title_right:
                loadData();
                break;
        }
    }

    private void loadData() {
        String id = getIntent().getStringExtra("uid");
        String newPwd = etNewPwd.getText().toString().trim();
        String oldPwd = etOldPwd.getText().toString().trim();
        String rePwd = etRePwd.getText().toString().trim();
        if (!oldPwd.equals(MyApp.user.getPwd())) {
            XUtils.showToast("旧密码输入错误请重新输入");
            return;
        }
        if (!newPwd.matches("^\\w{6,20}$")) {
            XUtils.showToast("新密码格式错误，请重新输入");
            return;
        }
        if (!rePwd.equals(newPwd)) {
            XUtils.showToast("两次输入密码不一致，请重新输入");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.uid", id);
        params.addBodyParameter("u.pwd", newPwd);
        httpHandler=XUtils.send(XUtils.UUSER, params, new MyCallBack<Result<Muser>>(new TypeReference<Result<Muser>>(){}) {

            @Override
            public void success(Result<Muser> result) {
                XUtils.showToast(result.desc);
                if (result.state == Result.STATE_SUC) {
                    MyApp.user = result.data;
                    finish();
                    endIntentAnim();
                }
            }
        },true);
    }
}
