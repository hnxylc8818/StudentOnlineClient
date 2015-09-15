package com.stuonline;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.stuonline.views.TitleView;

/**
 * ËïÎÀÐÇ£º×¢²áActivity
 * Created by Administrator on 2015/9/15.
 */
public class RegisterActivity extends Activity {
    private TitleView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTitle= (TitleView) findViewById(R.id.register_title);
        mTitle.setOnLeftclickListener(listener);
        mTitle.setOnRightTextclickListener(listener);


    }
    public View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.register_title:
                    break;

            }


        }
    };
}
