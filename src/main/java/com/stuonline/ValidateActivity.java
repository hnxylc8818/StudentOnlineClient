package com.stuonline;

import android.app.Activity;
import android.os.Bundle;

import cn.smssdk.SMSSDK;

/**
 * Created by SunJiShuang on 2015/9/15.
 */
public class ValidateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SMSSDK.initSDK(this, "a659ba5e877d", "72f1a4c705eb2e64c09aa13c9cbe3f89");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
    }
}
