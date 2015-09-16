package com.stuonline.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Xubin on 2015/9/10.
 */
public class SharedUtil {

    private static final String NAME="user.sp";
    public static final String FONT="font.sp";

    /**
     * 保存用户选择的模式
     * @param context
     * @param isNight
     */
    public static void saveModel(Context context,boolean isNight){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isNight",isNight);
        edit.commit();
    }

    /**
     * 从手机读取用户选择的模式
     * @param context
     * @return
     */
    public static boolean getModel(Context context){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean("isNight",false);
    }

    /**
     * 保存用户选择的字号
     * @param context
     * @param font
     */
    public static void saveFont(Context context,int font){
        SharedPreferences sp=context.getSharedPreferences(FONT,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("font",font);
        edit.commit();
    }

    /**
     * 读取用户选择的字号
     * @param context
     * @return
     */
    public static int getFont(Context context){
        SharedPreferences sp=context.getSharedPreferences(FONT,Context.MODE_PRIVATE);
        return sp.getInt("font",2);
    }

}
