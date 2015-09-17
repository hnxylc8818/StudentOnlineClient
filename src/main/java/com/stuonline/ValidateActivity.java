package com.stuonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.CustomerEditText;
import com.stuonline.views.TitleView;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by SunJiShuang on 2015/9/15.
 * 短信验证
 */
public class ValidateActivity extends BaseActivity {
    @ViewInject(R.id.validate_title)
    private TitleView title;
    @ViewInject(R.id.validate_account)
    private CustomerEditText etAccount;
    @ViewInject(R.id.validate_code)
    private CustomerEditText etCode;
    @ViewInject(R.id.validate_send_code)
    private Button btSendCode;
    private static int TIME = 30;
    private Timer timer;
    private TimerTask task;
    private int reg;

    private String account;
    private String code;
    private Handler msghandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    XUtils.showToast(msg.arg1);
                    break;
                case 2:
                    XUtils.showToast((String) msg.obj);
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            code = etCode.getText().toString().trim();
            account = etAccount.getText().toString().trim();
            switch (v.getId()) {
                case R.id.title_left:
                    finish();
                    endIntentAnim();
                    break;
                case R.id.title_right:
                    if (code.length() == 4) {
                        SMSSDK.submitVerificationCode("86", account, code);
                    }
                    break;
            }
        }
    };
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    msghandler.sendMessage(msghandler.obtainMessage(1, R.string.code_send_suc, 0));
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (reg == 1) {
                        Intent intent = new Intent(ValidateActivity.this, RegisterActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ValidateActivity.this, SmsRePwdActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                    finish();
                    startIntentAnim();
                }
            } else {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    msghandler.sendMessage(msghandler.obtainMessage(1, R.string.code_send_fail, 0));
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    msghandler.sendMessage(msghandler.obtainMessage(1, R.string.code_error, 0));
                }
            }
        }
    };


    @OnClick({R.id.validate_send_code})
    public void onClick(View v) {
        getCode();
        if (account.matches("^1(3|4|5|6|7|8)\\d{9}$")) {
            SMSSDK.getVerificationCode("86", account);
        } else {
            XUtils.showToast("手机号码格式错误");
        }
    }

    private void getCode() {
        btSendCode.setEnabled(false);
        // 设置定时任务
        task = new TimerTask() {
            @Override
            public void run() {
                // 当前线程不可以进行UI操作，new一个UI线程
                runOnUiThread(runnable);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            btSendCode.setText(String.format("%d秒后重新发送", TIME));
            etAccount.setEnabled(false);
            TIME--;
            if (TIME <= 0) {
                // 结束定时任务线程,取消定时
                task.cancel();
                TIME = 30;
                btSendCode.setEnabled(true);
                btSendCode.setText("发送验证码");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        SMSSDK.initSDK(this, "a659ba5e877d", "72f1a4c705eb2e64c09aa13c9cbe3f89");
        setContentView(R.layout.activity_validate);
        ViewUtils.inject(this);
        title.setOnRightTextclickListener(onClickListener);
        title.setOnLeftclickListener(onClickListener);
        SMSSDK.registerEventHandler(eventHandler);
        timer = new Timer();
        etAccount.setEtChangeLis(textWatcher);
        reg = getIntent().getIntExtra("reg", 2);
        account = etAccount.getText().toString().trim();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            RequestParams params;
            if (s.length() == 11 && etAccount.getText().matches("^1(3|4|5|7|8)\\d{9}$")) {
                account = etAccount.getText().toString().trim();
                if (reg == 1) {
                    params = new RequestParams();
                    params.addBodyParameter("u.account", account);
                    XUtils.send(XUtils.QUACCOUNT, params, new MyCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            if (responseInfo.result != null) {
                                JsonUtil<Result<Muser>> jsonUtil = new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>() {
                                });
                                Result<Muser> result = jsonUtil.parse(responseInfo.result);
                                if (result.state == Result.STATE_SUC) {
                                    XUtils.showToast("该账号已存在，请重新输入");
                                    btSendCode.setEnabled(false);
                                } else {
                                    btSendCode.setEnabled(true);
                                }
                            }
                        }
                    });
                }
//                else {
//                    btSendCode.setEnabled(true);
//                }

                if (reg == 2) {
                    params = new RequestParams();
                    params.addBodyParameter("u.account", account);
                    XUtils.send(XUtils.QUACCOUNT, params, new MyCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            if (responseInfo.result != null) {
                                JsonUtil<Result<Muser>> jsonUtil = new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>() {
                                });
                                Result<Muser> result = jsonUtil.parse(responseInfo.result);
                                if (result.state == Result.STATE_FAIL) {
                                    XUtils.showToast("该账号不存在，请重新输入");
                                    btSendCode.setEnabled(false);
                                } else {
                                    btSendCode.setEnabled(true);
                                }
                            }
                        }
                    });
                }
//                else {
//                    btSendCode.setEnabled(true);
//                }
            } else if (s.length() == 11) {
                XUtils.showToast("账号格式不正确");
                btSendCode.setEnabled(false);
            }
            btSendCode.setEnabled(false);
        }
    };
}
