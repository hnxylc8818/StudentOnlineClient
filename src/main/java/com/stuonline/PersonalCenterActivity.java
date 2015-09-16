package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.stuonline.entity.Feedback;
import com.stuonline.https.XUtils;
import com.stuonline.utils.FeedbackDialog;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.ParallaxListView;
import com.stuonline.views.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SunJiShuang on 2015/9/15.
 */
public class PersonalCenterActivity extends BaseActivity {

    private TextView personAccount;
    private TextView personNick;
    private TextView personSchool;
    @ViewInject(R.id.personal_center_title)
    private TitleView personTitle;
    @ViewInject(R.id.personal_center_lv)
    private ParallaxListView lv;

    private Intent intent;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        setContentView(R.layout.activity_personal_center);
        ViewUtils.inject(this);
        View header = getLayoutInflater().inflate(R.layout.layout_personal_center_header, null);
        personAccount = (TextView) header.findViewById(R.id.personal_center_account);
        personNick = (TextView) header.findViewById(R.id.personal_center_nick);
        personSchool = (TextView) header.findViewById(R.id.personal_center_school);
        ImageView imgBg = (ImageView) header.findViewById(R.id.header_bg);
        lv.addHeaderView(header);
        if (null != MyApp.user) {
            personAccount.setText(String.format("账号：%s", MyApp.user.getAccount()));
            personNick.setText(String.format("昵称：%s", MyApp.user.getNick()));
            personSchool.setText(String.format("学校：%s", MyApp.user.getSchool()));
        }
        loadData();
        String[] from = new String[]{"img", "text"};
        int[] to = new int[]{R.id.personal_center_header_img, R.id.personal_center_header_text};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.layout_personal_center_item, from, to);
        lv.setAdapter(adapter);
        lv.setParallaxImageView(imgBg);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PersonalCenterActivity.this, PersonageMessageActivity.class);
                startActivity(intent);
                startIntentAnim();
            }
        });
        personTitle.setOnLeftclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                endIntentAnim();
            }
        });
    }

    private void loadData() {
        data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("img", R.drawable.msg);
        map.put("text", "校内信息");
        data.add(map);
        map = new HashMap<>();
        map.put("img", R.drawable.safe);
        map.put("text", "账号与安全");
        data.add(map);
        map = new HashMap<>();
        map.put("img", R.drawable.setting);
        map.put("text", "系统设置");
        data.add(map);
        map = new HashMap<>();
        map.put("img", R.drawable.feedback);
        map.put("text", "意见反馈");
        data.add(map);
    }

    @OnItemClick(R.id.personal_center_lv)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                // 跳转校内信息
                Intent intent = new Intent(PersonalCenterActivity.this, ThuInfoActivity.class);
                startActivity(intent);
                startIntentAnim();
                break;
            case 2:
                // 跳转账号与安全
                startActivity(new Intent(PersonalCenterActivity.this,AccountAndSecurityActivity.class));
                startIntentAnim();
                break;
            case 3:
                // 跳转系统设置
                intent = new Intent(PersonalCenterActivity.this, SettingActivity.class);
                startActivity(intent);
                startIntentAnim();
                break;
            case 4:
                // 跳转意见反馈
                FeedbackDialog.showWaitting(this);
                break;
        }
    }

    @OnClick({R.id.personal_center_person_info, R.id.personal_center_exit})
    private void click(View v) {
        switch (v.getId()) {
            case R.id.personal_center_exit:
                // 退出登录，跳转登录页面,并注销当前账号
                MyApp.release();
                intent = new Intent(PersonalCenterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                endIntentAnim();
                break;
        }
    }

    // 当界面显示出来的时候回调
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            lv.setViewBounds();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTheme();
        init();
    }
}
