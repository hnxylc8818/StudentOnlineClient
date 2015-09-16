package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.TitleView;

/**
 * 孙卫星：账号与安全
 * Created by Administrator on 2015/9/15.
 */
public class AccountAndSecurityActivity extends BaseActivity {
    @ViewInject(R.id.aas_pwd_photo)
    private ImageView mUpdatePwdImg;
    @ViewInject(R.id.aas_account)
    private TextView mSecAccount;
    @ViewInject(R.id.aas_email)
    private TextView mSecEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_accoutandsecurity);
        ViewUtils.inject(this);

    }
    @OnClick({R.id.title_left,R.id.aas_pwd_photo})
    public void onclick(View v){
        switch(v.getId()){
            case R.id.title_left:
                //返回上一页
                finish();
                endIntentAnim();
                break;
            case R.id.aas_pwd_photo:
                //修改密码
                startActivity(new Intent(AccountAndSecurityActivity.this,UpdatePasswordActivity.class));
                startIntentAnim();
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
