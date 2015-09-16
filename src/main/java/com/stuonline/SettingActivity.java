package com.stuonline;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.TitleView;

/**
 * Created by Xubin on 2015/9/15.
 */
public class SettingActivity extends BaseActivity {

    private RadioButton rbSmall;
    private RadioButton rbMiddle;
    private RadioButton rbBig;
    private CheckBox cbNight;
    private TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        if (SharedUtil.getModel(this)) {
            setTheme(R.style.night);
        } else {
            setTheme(R.style.def);
        }
        setContentView(R.layout.activity_setting);
        cbNight = (CheckBox) findViewById(R.id.setting_model);
        rbSmall = (RadioButton) findViewById(R.id.setting_font_small);
        rbMiddle = (RadioButton) findViewById(R.id.setting_font_middle);
        rbBig = (RadioButton) findViewById(R.id.setting_font_big);
        title = (TitleView) findViewById(R.id.setting_title);
        cbNight.setChecked(SharedUtil.getModel(this));
        cbNight.setOnCheckedChangeListener(checkedChangeListener);
        title.setOnLeftclickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedUtil.saveModel(SettingActivity.this, isChecked);
            init();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
        init();
    }
}
