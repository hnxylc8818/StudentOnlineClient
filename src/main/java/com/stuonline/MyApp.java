package com.stuonline;

import android.app.Application;
import com.stuonline.entity.MeTab;
import com.stuonline.entity.Muser;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DBTools;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.SharedUtil;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Xubin on 2015/9/8.
 */
public class MyApp extends Application {
    public static Muser user;
    public static final int SEX = 1;
    public static final int PHOTO = 2;
    public static final int ROLE = 3;
    public static final int RESET = 4;
    public static int type = 0;
    public static boolean isWelcome = true;   // 是否开启欢迎界面

    public static boolean isMainChange = false;
    private static List<MeTab> meTabs;
    private static DBTools dbTools;
    public static boolean isNight;


    @Override
    public void onCreate() {

        super.onCreate();
        XUtils.init(getApplicationContext());
        DialogUtil.init(getApplicationContext());
        dbTools = new DBTools(getApplicationContext());
        isNight=SharedUtil.getModel(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static void release() {
        user = null;
        meTabs = null;
    }

    public static List<MeTab> getMeTabs() {
        if (meTabs == null) {

            meTabs = dbTools.queryAllisMe("1");
        }

        return meTabs;
    }

    public static void setMeTabs(List<MeTab> meTabs) {
        MyApp.meTabs = meTabs;
    }
}
