package com.stuonline;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.lidroid.xutils.http.HttpHandler;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.SharedUtil;

/**
 * Created by Xubin on 2015/9/8.
 */
public class BaseActivity extends FragmentActivity {

    protected HttpHandler httpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtil.destoryWaitting();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void changeTheme(){
        boolean isNight = SharedUtil.getModel(this);
        if (isNight) {
            setTheme(R.style.night);
        } else {
            setTheme(R.style.def);
        }
    }
}
