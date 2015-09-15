package com.stuonline;

import android.os.Bundle;

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
        boolean isNight= SharedUtil.getModel(this);
        if (isNight){
            setTheme(R.style.night);
        } else {
            setTheme(R.style.def);
        }
        init();
    }
    private void init() {
        setContentView(R.layout.activity_updatepassword);
    }
}
