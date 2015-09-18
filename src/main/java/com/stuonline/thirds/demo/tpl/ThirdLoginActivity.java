package com.stuonline.thirds.demo.tpl;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.stuonline.BaseActivity;
import com.stuonline.MyApp;
import com.stuonline.PersonalCenterActivity;
import com.stuonline.R;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.thirds.tpl.OnLoginListener;
import com.stuonline.thirds.tpl.ThirdPartyLogin;
import com.stuonline.thirds.tpl.UserInfo;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;

public class ThirdLoginActivity extends BaseActivity {
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "27fe7909f8e8";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "3c5264e7e05b8860a9b98b34506cfa6e";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDemo();
        finish();
    }

    private void showDemo() {
        ThirdPartyLogin tpl = new ThirdPartyLogin();
        tpl.setSMSSDKAppkey(APPKEY, APPSECRET);
        tpl.setOnLoginListener(new OnLoginListener() {
            public boolean onSignin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                return true;
            }

            public boolean onSignUp(UserInfo info) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                File photo=new File(info.getUserIcon());
                int gender=1;
                if (info.getUserGender()== UserInfo.Gender.BOY){
                    gender=1;
                }else if (info.getUserGender()== UserInfo.Gender.GIRL){
                    gender=0;
                }
                RequestParams params=new RequestParams();
                params.addBodyParameter("u.account",info.getUserNote());
                params.addBodyParameter("u.nick",info.getUserName());
                params.addBodyParameter("u.gender", String.valueOf(gender));
                if (null != photo && photo.exists()) {
                    params.addBodyParameter("photo", photo);
                }
                XUtils.send(XUtils.REGTHIRD, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (null != responseInfo){
                            JsonUtil<Result<Muser>> jsonUtil=new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>(){});
                            Result<Muser> result=jsonUtil.parse(responseInfo.result);
                            XUtils.showToast(result.desc);
                            if (result.state==Result.STATE_SUC){
                                MyApp.user=result.data;
                                Intent intent=new Intent(ThirdLoginActivity.this, PersonalCenterActivity.class);
                                startActivity(intent);
                                finish();
                                startIntentAnim();
                            }
                        }else{
                            XUtils.showToast("发生错误");
                        }
                    }
                });
                return true;
            }
        });
        tpl.show(this);
    }

}
