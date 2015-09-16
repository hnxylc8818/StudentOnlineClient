package com.stuonline;

import android.os.Bundle;

import com.stuonline.utils.SharedUtil;

/**
 * 孙卫星：账号与安全
 * Created by Administrator on 2015/9/15.
 */
public class AccountAndSecurityActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_accoutandsecurity);
    }
    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
        init();
    }
}
