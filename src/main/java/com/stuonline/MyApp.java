package com.stuonline;

import android.app.Application;

import com.stuonline.entity.Muser;
import com.stuonline.https.XUtils;

/**
 * Created by Xubin on 2015/9/8.
 */
public class MyApp extends Application {
    public static Muser user;
    public static final int SEX=1;
    public static final int PHOTO=2;
    public static final int ROLE=3;
    public static final int RESET=4;
    public static int type=0;
    public static boolean isWelcome=true;   // 是否开启欢迎界面
    public static boolean isChange=false;

    @Override
    public void onCreate() {
        super.onCreate();
        XUtils.init(getApplicationContext());
    }

    public static void release() {
        user = null;
    }
}
