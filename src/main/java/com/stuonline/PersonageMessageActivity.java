package com.stuonline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.CircleImage;
import com.stuonline.views.TitleView;

/**
 * 孙卫星：个人信息
 * Created by Administrator on 2015/9/15.
 */
public class PersonageMessageActivity extends BaseActivity{
    @ViewInject(R.id.pgemge_title)
    private TitleView mPersonTitle;
    @ViewInject(R.id.pgemge_photo)
    private CircleImage mPersonPhoto;
    @ViewInject(R.id.pgemge_name)
    private EditText mPersonName;
    @ViewInject(R.id.pgemge_nick)
    private EditText mPersonNick;
    @ViewInject(R.id.pgemge_sex)
    private EditText mPersonSex;
    @ViewInject(R.id.pgemge_save)
    private Button mPersonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_personage_message);
        ViewUtils.inject(this);

    }
    @OnClick({R.id.pgemge_photo,R.id.pgemge_save,R.id.title_left})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.title_left:
                //返回上一层
                finish();
                endIntentAnim();
                break;
            case R.id.pgemge_photo:
                //更改头像
                break;
            case R.id.pgemge_save:
                //保存
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
        init();
    }
}
