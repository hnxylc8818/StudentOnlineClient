package com.stuonline;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.EditDialog;
import com.stuonline.utils.JsonUtil;
import com.stuonline.views.SelectPicPopupWindow;
import com.stuonline.views.TitleView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by SunJiShuang on 2015/9/16.
 * 校内信息
 */
public class ThuInfoActivity extends BaseActivity {
    private TitleView title;
    private SelectPicPopupWindow menuWindow;
    @ViewInject(R.id.thuinfo_role_tv)
    private TextView role;
    @ViewInject(R.id.thuinfo_school_tv)
    private TextView school;
    @ViewInject(R.id.thuinfo_faculty_tv)
    private TextView department;
    @ViewInject(R.id.thuinfo_class_tv)
    private TextView uclass;
    @ViewInject(R.id.thuinfo_date_tv)
    private TextView thuinfo_date_tv;
    @ViewInject(R.id.thuinfo_bt)
    private Button thuinfo_bt;
    private int roleId;
    private SimpleDateFormat df;
    private Calendar calendar;
    private String schoolAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        setContentView(R.layout.activity_thuinfo);
        ViewUtils.inject(this);
        calendar = Calendar.getInstance();
        title = (TitleView) findViewById(R.id.thuinfo_title);
        title.setOnLeftclickListener(onClickListener);
        if (MyApp.user != null) {
            if (null != MyApp.user.getRoleId()) {
                role.setText(MyApp.user.getRoleId() == 1 ? "学生" : "教师");
            } else {
                role.setText("未选择");
            }
            if (!TextUtils.isEmpty(schoolAdd)) {
                MyApp.user.setSchool(schoolAdd);
            }
            school.setText(MyApp.user.getSchool());
            if (null != MyApp.user.getDepartment()) {
                department.setText(MyApp.user.getDepartment());
            } else {
                department.setText("未填写院系");
            }
            if (null != MyApp.user.getUClass()) {
                uclass.setText(MyApp.user.getUClass());
            } else {
                uclass.setText("未填写班级");
            }
            if (null != MyApp.user.getUyear()) {
                thuinfo_date_tv.setText(MyApp.user.getUyear());
            } else {
                thuinfo_date_tv.setText("未选择年份");
            }
        }


    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    private void updateDate() {
        df = new SimpleDateFormat("yyyy-MM-dd");
        thuinfo_date_tv.setText(df.format(calendar.getTime()));
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

    @OnClick({R.id.huinfo_role, R.id.thuinfo_school, R.id.thuinfo_faculty, R.id.thuinfo_class, R.id.thuinfo_date, R.id.thuinfo_bt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huinfo_role:
                //角色
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(ThuInfoActivity.this, itemsOnClick, MyApp.ROLE);
                //显示窗口
                menuWindow.showAtLocation(ThuInfoActivity.this.findViewById(R.id.thuinfo_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.thuinfo_school:
                //学校
                Intent intent = new Intent(this, SchoolSearchActivity.class);
                startActivityForResult(intent, 20);
                break;
            case R.id.thuinfo_faculty:
                //院系
                EditDialog.showDialog(this, "请输入院系");
                EditDialog.setButtonEnsure(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String faculty = EditDialog.getText();
                        if (faculty.isEmpty()) {
                            department.setText(MyApp.user.getDepartment() == null ? "未填写" : MyApp.user.getDepartment());
                            EditDialog.hiddenWaitting();
                            return;
                        }
                        department.setText(faculty);
                        EditDialog.hiddenWaitting();
                    }
                });
                break;
            case R.id.thuinfo_class:
                //班级
                EditDialog.showDialog(this, "请输入班级");
                EditDialog.setButtonEnsure(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ulass = EditDialog.getText();
                        if (ulass.isEmpty()) {
                            uclass.setText(MyApp.user.getNick() == null ? "未填写" : MyApp.user.getNick());
                            EditDialog.hiddenWaitting();
                            return;
                        }
                        uclass.setText(ulass);
                        EditDialog.hiddenWaitting();
                    }
                });
                break;
            case R.id.thuinfo_date:
                //入学时间
                // 构建一个 DatePickerDialog 并显示
                new DatePickerDialog(ThuInfoActivity.this,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            case R.id.thuinfo_bt:
                saveThuInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == 30 && data != null) {
            schoolAdd = data.getStringExtra("address");
        }
    }

    private void saveThuInfo() {
        String schools = school.getText().toString();
        String departments = department.getText().toString();
        String ulcass = uclass.getText().toString();
        String date = thuinfo_date_tv.getText().toString();
        if (TextUtils.isEmpty(role.getText().toString())) {
            XUtils.showToast("请选择角色");
            return;
        } else if (TextUtils.isEmpty(schools)) {
            XUtils.showToast("请选择学校");
            return;
        } else if (TextUtils.isEmpty(departments)) {
            XUtils.showToast("请选择院系");
            return;
        } else if (TextUtils.isEmpty(ulcass)) {
            XUtils.showToast("请选择班级");
            return;
        } else if (TextUtils.isEmpty(date)) {
            XUtils.showToast("请选择入学年份");
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("u.uid", String.valueOf(MyApp.user.getUid()));
        params.addBodyParameter("u.roleId", String.valueOf(roleId));
        params.addBodyParameter("u.school", schools);
        params.addBodyParameter("u.department", departments);
        params.addBodyParameter("u.UClass", ulcass);
        params.addBodyParameter("u.uyear", date);
        DialogUtil.showWaitting(this);
        httpHandler=XUtils.send(XUtils.UUSER, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DialogUtil.hiddenWaitting();
                if (responseInfo != null) {
                    JsonUtil<Result<Muser>> jsonUtil = new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>() {
                    });
                    Result<Muser> result = jsonUtil.parse(responseInfo.result);
                    XUtils.showToast(result.desc);
                    if (result.state == Result.STATE_SUC) {
                        MyApp.user = result.data;
                        finish();
                        endIntentAnim();
                    }
                }
            }
        });

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_top:
                    roleId = 1;
                    role.setText("学生");
                    break;
                case R.id.btn_bottom:
                    roleId = 2;
                    role.setText("教师");
                    break;
                default:
                    break;
            }
        }

    };

}
