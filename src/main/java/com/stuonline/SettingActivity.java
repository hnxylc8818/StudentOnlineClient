package com.stuonline;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.stuonline.utils.SharedUtil;
import com.stuonline.views.TitleView;

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

    private int font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

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
                MyApp.isChange=true;
                SharedUtil.saveFont(SettingActivity.this,font);
                init();
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            endIntentAnim();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MyApp.isChange=true;
            SharedUtil.saveModel(SettingActivity.this, isChecked);
            init();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
