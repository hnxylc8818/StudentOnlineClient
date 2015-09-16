package com.stuonline.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.internal.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.stuonline.MyApp;
import com.stuonline.PersonalCenterActivity;
import com.stuonline.R;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;

/**
 * Created by SunJiShuang on 2015/9/15.
 */
public class FeedbackDialog {
    public static Dialog waitting;
    private static EditText mFeedEdit;

    /**
     * 显示对话框
     */
    public static void showWaitting(Context context) {
        if (waitting == null) {
            waitting = new AlertDialog.Builder(context).create();
            waitting.setCanceledOnTouchOutside(true);  // 触摸边缘不消失
            waitting.show();
            Window window = waitting.getWindow();
            View v = LayoutInflater.from(context).inflate(R.layout.layout_dialog_feedback, null);
            v.setBackgroundColor(Color.WHITE);
//            v.setPadding(30, 30, 30, 30);
            window.setContentView(v);
            Button button = (Button) waitting.findViewById(R.id.feedback_bt);
            mFeedEdit = (EditText) v.findViewById(R.id.feedback_edit);
            button.setOnClickListener(onClickListener);
        } else {
            waitting.show();
        }
    }

    private static View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = mFeedEdit.getText().toString().trim();
            if (content.isEmpty()) {
                XUtils.showToast("意见反馈不能为空!");
                return;
            }
            RequestParams params = new RequestParams();
            params.addBodyParameter("f.content", content);
            params.addBodyParameter("f.uid", String.valueOf(MyApp.user.getUid()));
            XUtils.send(XUtils.SFB, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo != null) {
                        JsonUtil<Result<Boolean>> jsonUtil = new JsonUtil<Result<Boolean>>(new TypeReference<Result<Boolean>>() {
                        });
                        Result<Boolean> result = jsonUtil.parse(responseInfo.result);
                        XUtils.showToast(result.desc);
                        if (result.data) {
                            hiddenWaitting();
                        }
                    }
                }
            });
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
