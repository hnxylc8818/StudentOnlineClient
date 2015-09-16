package com.stuonline.controller;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Xubin on 2015/7/16.
 */
public class ActivityController {

    private static List<Activity> activities = new LinkedList<Activity>();

    // 添加活动
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    // 移除活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    // 关闭所有活动
    public static void exit() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
