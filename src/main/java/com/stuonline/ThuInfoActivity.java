package com.stuonline;

import android.os.Bundle;
import android.view.View;

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
        changeTheme();
        init();
    }
}
