package com.stuonline;

import android.os.Bundle;
import android.text.TextUtils;
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
 * Created by Administrator on 2015/9/17.
 * 验证邮箱找回密码(重置密码页)
 */
public class ResetPasswordActivity extends BaseActivity {
    @ViewInject(R.id.verify_title)
    private TitleView verfytitle;
    @ViewInject(R.id.verify_email)
    private CustomerEditText verifytv;
    @ViewInject(R.id.verify_pwd)
    private CustomerEditText verifypwd;
    @ViewInject(R.id.verify_repeatepwd)
    private CustomerEditText verifyrepstpwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        setContentView(R.layout.activity_verify);
        ViewUtils.inject(this);

    }
    @OnClick({R.id.title_left,R.id.title_right,})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_left:
                finish();
                startIntentAnim();
                break;
            case R.id.title_right:
                updatepwd();
                break;
        }
    }
    public void updatepwd(){
        String Verify=verifytv.getText().toString();
        String pwd=verifypwd.getText().toString();
        String repeatPwd=verifyrepstpwd.getText().toString();
        if (!Verify.equals(MyApp.user.getBundle())){
            XUtils.showToast("验证码错误");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            XUtils.showToast("密码不能为空");
            return;
        }

        if (!pwd.equals(repeatPwd)){
            XUtils.showToast("密码不一致!");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.uid", String.valueOf(MyApp.user.getUid()));
        params.addBodyParameter("u.pwd", pwd);
        DialogUtil.showWaitting(this);
        XUtils.send(XUtils.UUSER, params, new MyCallBack<String>() {
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
                        finish();
                        endIntentAnim();
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
