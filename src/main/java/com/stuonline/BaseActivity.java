package com.stuonline;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.lidroid.xutils.http.HttpHandler;
import com.stuonline.controller.ActivityController;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.EditDialog;
import com.stuonline.utils.SharedUtil;

/**
 * Created by Xubin on 2015/9/8.
 */
public class BaseActivity extends FragmentActivity {
    private boolean isExit;
    private Handler handler = new Handler();
    protected HttpHandler httpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeTheme();
        ActivityController.addActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果点击返回键并且Waitting对话框是显示状态，取消网络连接
        if (keyCode == KeyEvent.KEYCODE_BACK && DialogUtil.isWaittingShowed()) {
            if (null != httpHandler && !httpHandler.isCancelled()) {
                httpHandler.cancel();
                httpHandler = null;
                return true;
            }

        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtil.destoryWaitting();
        EditDialog.destoryWaitting();
        ActivityController.removeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
    }

    public void changeTheme() {
        boolean isNight = SharedUtil.getModel(this);
        if (isNight) {
            setTheme(R.style.night);
        } else {
            setTheme(R.style.def);
        }
        int font = SharedUtil.getFont(this);
        switch (font) {
            case 1:
                setTheme(R.style.small_size);
                break;
            case 2:
                setTheme(R.style.middle_size);
                break;
            case 3:
                setTheme(R.style.big_size);
                break;
        }
    }

    protected void startIntentAnim() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    protected void endIntentAnim() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 退出程序，关闭所有活动
     */
    public void exit() {
        if (!isExit) {
            isExit = true;
            XUtils.showToast("双击Back键退出程序");
            handler.postDelayed(runnable, 1500);
        } else {
            ActivityController.exit();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };

}
