package com.stuonline.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.stuonline.R;

/**
 * Created by SunJiShuang on 2015/9/15.
 */
public class DialogHelper {
    public static Dialog waitting;

    /**
     * 显示对话框
     */
    public static void showWaitting(Context context) {
        if (waitting == null) {
            waitting = new AlertDialog.Builder(context).create();
            waitting.setCanceledOnTouchOutside(false);  // 触摸边缘不消失
            waitting.show();
            Window window = waitting.getWindow();
            window.setContentView(R.layout.layout_dialog_helper);
            Button button = (Button) waitting.findViewById(R.id.select_dialog_helper_bt);
            button.setOnClickListener(onClickListener);
        } else {
            waitting.show();
        }
    }

    private static View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hiddenWaitting();
        }
    };

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

    public static boolean isWaittingShowed() {
        if (null != waitting && waitting.isShowing()) {
            return true;
        }
        return false;
    }
}
