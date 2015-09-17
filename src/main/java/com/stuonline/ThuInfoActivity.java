package com.stuonline;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
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
    @ViewInject(R.id.thuinfo_date_tv)
    private TextView thuinfo_date_tv;

    private int roleId;
    private SimpleDateFormat df;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_thuinfo);
        ViewUtils.inject(this);
        calendar = Calendar.getInstance();
        title = (TitleView) findViewById(R.id.thuinfo_title);
        title.setOnLeftclickListener(onClickListener);

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

    @OnClick({R.id.huinfo_role, R.id.thuinfo_school, R.id.thuinfo_faculty, R.id.thuinfo_class, R.id.thuinfo_date})
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
                break;
            case R.id.thuinfo_faculty:
                //院系
                break;
            case R.id.thuinfo_class:
                //班级
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
        }
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
