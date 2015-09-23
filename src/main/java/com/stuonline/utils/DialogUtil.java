package com.stuonline.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.stuonline.R;

/**
 * Created by Xubin on 2015/9/8.
 */
public class DialogUtil {
    public static Dialog waitting;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }
    /**
     * 返回一个列表对话框
     * @param context   上下文
     * @param items     列表项
     * @param title     对话框标题
     * @param clickListener 列表项点击监听
     * @return
     */
    public static AlertDialog.Builder getListDialogBuilder(Context context,
                                                           String[] items, String title,
                                                           DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context).setTitle(title).setItems(items, clickListener);

    }
    /**
     * 显示进度对话框
     */
    public static void showWaitting() {
        if (waitting == null) {
            waitting = new AlertDialog.Builder(mContext).create();
            waitting.setCanceledOnTouchOutside(false);  // 触摸边缘不消失
            Window window = waitting.getWindow();
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            waitting.show();
            window.setContentView(R.layout.layout_waitting);

        } else {
            waitting.show();
        }
    }


    /**
     * 隐藏
     */
    public static void hiddenWaitting() {
        if (null != waitting && waitting.isShowing()) {
            waitting.dismiss();
        }
    }

    /**
     * 销毁
     */
    public static void destoryWaitting() {
        hiddenWaitting();
        waitting = null;
    }

    public static boolean isWaittingShowed(){
        if(null != waitting && waitting.isShowing()){
            return true;
        }
        return false;
    }
}
