package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
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

    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeTheme();
        ActivityController.addActivity(this);
        detector = new GestureDetector(this, cestureLis);
    }

    // 分发触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private GestureDetector.OnGestureListener cestureLis = new GestureDetector.OnGestureListener() {

        // 按下时
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        // 短按时
        @Override
        public void onShowPress(MotionEvent e) {
        }

        // 抬起时
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        //滑动中持续调用 参数1 一次滑动前事件，2 一次滑动后事件，3 x滑动距离，4 y滑动距离
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        //长按时
        @Override
        public void onLongPress(MotionEvent e) {
        }

        //滑动结束后 参数1一次滑动前事件，2一次滑动后事件，3x滑动速度，4y滑动速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x1 = e1.getX();
            float x2 = e2.getX();
            float xDriver = x1 - x2;
            if (xDriver < -300) {
                // 如果当前Activity对象是MainActivity，不执行左滑退出事件
                if (BaseActivity.this instanceof MainActivity){
                    return false;
                }
                if (BaseActivity.this instanceof PersonalCenterActivity && MyApp.isMainChange){
                    Intent intent=new Intent(BaseActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                if (BaseActivity.this instanceof AddTabActivity && MyApp.isMainChange){
                    Intent intent=new Intent(BaseActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                finish();
                endIntentAnim();
            }
            return false;
        }
    };

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
            MyApp.isWelcome=true;
//            XUtils.release();
            MyApp.release();
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
