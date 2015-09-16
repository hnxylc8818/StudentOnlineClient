package com.stuonline;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
import com.stuonline.views.TitleView;

/**
 * Created by SunJiShuang on 2015/9/16.
 * 校内信息
 */
public class ThuInfoActivity extends BaseActivity {
    private TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_thuinfo);
        ViewUtils.inject(this);
        title = (TitleView) findViewById(R.id.thuinfo_title);
        title.setOnLeftclickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            endIntentAnim();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @OnClick({R.id.huinfo_role, R.id.thuinfo_school, R.id.thuinfo_faculty, R.id.thuinfo_class, R.id.thuinfo_date})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huinfo_role:
                //角色z
                break;
            case R.id.thuinfo_school:
                //学校
                break;
            case R.id.thuinfo_faculty:
                //院系
                break;
            case R.id.thuinfo_class:
                //班级
                break;
            case R.id.thuinfo_date:
                //入学时间
                break;
        }
    }
}
