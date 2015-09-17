package com.stuonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.JsonUtil;
import com.stuonline.views.CircleImage;
import com.stuonline.views.CircleRefreshLayout;
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
    private CircleImage personPhoto;
    @ViewInject(R.id.personal_center_title)
    private TitleView personTitle;
    @ViewInject(R.id.personal_center_lv)
    private ListView lv;
    private Dialog dialog;
    private Intent intent;
    private List<Map<String, Object>> data;
    private EditText etContent;
    private TextView hasnum;// 用来显示剩余字数
    private int num = 140;//限制的最大字数
    private CharSequence temp;
    private int selectionStart;
    private int selectionEnd;

    @ViewInject(R.id.personal_center_refresh)
    private CircleRefreshLayout refreshLayout;

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
        personPhoto = (CircleImage) header.findViewById(R.id.personal_center_photo);
        ImageView imgBg = (ImageView) header.findViewById(R.id.header_bg);

        lv.addHeaderView(header);
        if (null != MyApp.user) {
            personAccount.setText(String.format("账号：%s", MyApp.user.getAccount()));
            String nick = "";
            String school = "";
            if (MyApp.user.getNick() == null) {
                nick = "未填写";
            } else {
                nick = MyApp.user.getNick();
            }
            if (MyApp.user.getSchool() == null) {
                school = "未填写";
            } else {
                school = MyApp.user.getSchool();
            }
            personNick.setText(String.format("昵称：%s", nick));
            personSchool.setText(String.format("学校：%s", school));
            XUtils.bitmapUtils.display(personPhoto, XUtils.BURL + MyApp.user.getPhotoUrl());
        }
        loadData();
        String[] from = new String[]{"img", "text"};
        int[] to = new int[]{R.id.personal_center_item_img, R.id.personal_center_item_text};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.layout_personal_center_item, from, to);
        lv.setAdapter(adapter);
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
        refreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {

            }

            @Override
            public void refreshing() {

                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新完后调用该方法结束刷新
                        refreshLayout.finishRefreshing();
                    }
                },3000);
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
                intent = new Intent(PersonalCenterActivity.this, ThuInfoActivity.class);
                startActivity(intent);
                startIntentAnim();
                break;
            case 2:
                // 跳转账号与安全
                startActivity(new Intent(PersonalCenterActivity.this, AccountAndSecurityActivity.class));
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
                thuInfo();
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

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void thuInfo() {
        if (dialog == null) {
            View v = LayoutInflater.from(PersonalCenterActivity.this).inflate(
                    R.layout.layout_feedback, null);
            v.setBackgroundColor(Color.WHITE);
            etContent = (EditText) v.findViewById(R.id.feedback_edit);
            hasnum = (TextView) v.findViewById(R.id.feedback_num);
            Button sendFeedBack = (Button) v.findViewById(R.id.feedback_bt);
            hasnum.setText(num + "");
            sendFeedBack.setOnClickListener(onClickListener);
            etContent.addTextChangedListener(textWatcher);
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    PersonalCenterActivity.this);
            builder.setView(v);
            etContent.getText().clear();
            dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
        } else {
            etContent.getText().clear();
            dialog.show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                XUtils.showToast("意见反馈不能为空!");
                return;
            }
            RequestParams params = new RequestParams();
            params.addBodyParameter("f.content", content);
            params.addBodyParameter("f.uid", String.valueOf(MyApp.user.getUid()));
            XUtils.send(XUtils.SFB, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo != null) {
                        JsonUtil<Result<Boolean>> jsonUtil = new JsonUtil<Result<Boolean>>(new TypeReference<Result<Boolean>>() {
                        });
                        Result<Boolean> result = jsonUtil.parse(responseInfo.result);
                        XUtils.showToast(result.desc);
                        if (result.data) {
                            dialog.dismiss();
                            etContent.getText().clear();
                        }
                    }
                }
            });
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            int number = num - s.length();
            hasnum.setText("" + number);
            selectionStart = etContent.getSelectionStart();
            selectionEnd = etContent.getSelectionEnd();
            if (temp.length() > num) {
                s.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionEnd;
                etContent.setText(s);
                etContent.setSelection(tempSelection);//设置光标在最后
            }
        }

    };
}
