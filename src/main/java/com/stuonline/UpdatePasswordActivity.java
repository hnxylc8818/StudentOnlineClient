package com.stuonline;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.utils.SharedUtil;

/**
 * 孙卫星：修改密码
 * Created by Administrator on 2015/9/15.
 */
public class UpdatePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
        init();
    }
    private void init() {
        setContentView(R.layout.activity_updatepassword);
        ViewUtils.inject(this);
    }
    @OnClick({R.id.title_left,R.id.aas_pwd_photo})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.title_left:
                //返回上一页
                finish();
                endIntentAnim();
                break;
            case R.id.title_right:
                break;
        }

    }
}
