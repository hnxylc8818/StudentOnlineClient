package com.stuonline.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stuonline.R;

/**
 * Created by SunJiShuang on 2015/9/17.
 */
public class EditDialog {
    private static Dialog dialog;
    private static Button ensure; //确定
    private static Button cancel;//取消
    private static EditText input;//取消

    public static void showDialog(Context context, String text) {
        if (dialog == null) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.layout_edit_dialog, null);
            v.setBackgroundColor(Color.WHITE);
            TextView tv = (TextView) v.findViewById(R.id.edit_dialog_tv);
            tv.setText(text);
            ensure = (Button) v.findViewById(R.id.edit_dialog_ensure);
            cancel = (Button) v.findViewById(R.id.edit_dialog_cancel);
            input = (EditText) v.findViewById(R.id.edit_dialog_input);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setView(v);
            dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
        } else {
            dialog.show();
        }
    }

    /**
     * 确定按钮点击事件
     *
     * @param listener
     */

    public static void setButtonEnsure(View.OnClickListener listener) {
        ensure.setOnClickListener(listener);
    }

    /**
     * 获取EditText输入的值
     *
     * @return
     */
    public static String getText() {
        return input.getText().toString().trim();
    }

    /**
     * 隐藏
     */
    public static void hiddenWaitting() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            input.getText().clear();
        }
    }

    public static void destoryWaitting() {
        hiddenWaitting();
        dialog = null;
    }
}
