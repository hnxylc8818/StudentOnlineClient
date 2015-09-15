package com.stuonline;

import android.app.Application;

import com.stuonline.entity.Muser;
import com.stuonline.https.XUtils;

/**
 * Created by Xubin on 2015/9/8.
 */
public class MyApp extends Application {
    public static Muser user;

    @Override
    public void onCreate() {
        super.onCreate();
        XUtils.init(getApplicationContext());
    }

    public static void release() {
        user = null;
    }
}
