package com.stuonline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stuonline.entity.AppVersion;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.TitleView;

import cn.sharesdk.framework.authorize.ResizeLayout;

/**
 * Created by Xubin on 2015/9/15.
 */
public class SettingActivity extends BaseActivity {

    private RadioGroup rg;
    private RadioButton rbSmall;
    private RadioButton rbMiddle;
    private RadioButton rbBig;
    private CheckBox cbNight;
    private TitleView title;
    private String currVersionName;
    private AppVersion appVersion;
    private RelativeLayout setting_check_update;

    private int font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {

        changeTheme();
        setContentView(R.layout.activity_setting);
        rg= (RadioGroup) findViewById(R.id.setting_rg);
        cbNight = (CheckBox) findViewById(R.id.setting_model);
        rbSmall = (RadioButton) findViewById(R.id.setting_font_small);
        rbMiddle = (RadioButton) findViewById(R.id.setting_font_middle);
        rbBig = (RadioButton) findViewById(R.id.setting_font_big);
        title = (TitleView) findViewById(R.id.setting_title);
        cbNight.setChecked(SharedUtil.getModel(this));
        cbNight.setOnCheckedChangeListener(checkedChangeListener);
        title.setOnLeftclickListener(onClickListener);
        setting_check_update= (RelativeLayout) findViewById(R.id.setting_check_update);
        setting_check_update.setOnClickListener(onClickListener);
        font=SharedUtil.getFont(this);
        switch (font){
            case 1:
                rbSmall.setChecked(true);
                break;
            case 2:
                rbMiddle.setChecked(true);
                break;
            case 3:
                rbBig.setChecked(true);
                break;
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.setting_font_small:
                        font=1;
                        break;
                    case R.id.setting_font_middle:
                        font=2;
                        break;
                    case R.id.setting_font_big:
                        font=3;
                        break;
                }
                setChange();
                SharedUtil.saveFont(SettingActivity.this,font);
                init();
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_left:
                    finish();
                    endIntentAnim();
                    break;
                case R.id.setting_check_update:
                    getUpdate();
                    break;
            }

        }
    };
    public boolean getUpdate() {
        try {
            PackageManager m = this.getPackageManager();
            PackageInfo info = m.getPackageInfo(this.getPackageName(), 0);
            currVersionName = info.versionName;
            Log.i("aaaaa",String.valueOf(info.versionCode));
            XUtils.showToast(info.versionCode);
            RequestParams params = new RequestParams();

            params.addBodyParameter("ver", String.valueOf(info.versionCode));
            DialogUtil.showWaitting(this);
            XUtils.send(XUtils.VER, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    DialogUtil.hiddenWaitting();
                    if (null != responseInfo) {
                        JsonUtil<Result<AppVersion>> jsonUtil = new JsonUtil<Result<AppVersion>>(new TypeReference<Result<AppVersion>>() {
                        });
                        Result<AppVersion> result = jsonUtil.parse(responseInfo.result);
                        XUtils.showToast(result.desc);
                        if (result.state == Result.STATE_SUC) {
                            appVersion = result.data;
                            showDownload();
                        }
                    } else {
                        XUtils.showToast("更新错误");
                    }
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            XUtils.showToast("更新错误");
        }

        return true;
    }

    public void showDownload() {
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage(String.format("当前版本%s,最新版本%s，是否下载更新", currVersionName, appVersion.getVersionName()))
                .setNegativeButton("立即更新", dialogLis).setNeutralButton("下次再说", dialogLis).show();
    }

    private DialogInterface.OnClickListener dialogLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    XUtils.download(appVersion.getAppUrl());
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setChange();
            SharedUtil.saveModel(SettingActivity.this, isChecked);
            init();
        }
    };

    private void setChange(){
        MyApp.isMainChange=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();

    }


}
