package com.stuonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.CircleImage;
import com.stuonline.views.SelectPicPopupWindow;
import com.stuonline.views.TitleView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 孙卫星：个人信息
 * Created by Administrator on 2015/9/15.
 */
public class PersonageMessageActivity extends BaseActivity {
    @ViewInject(R.id.pgemge_title)
    private TitleView mPersonTitle;
    @ViewInject(R.id.pgemge_photo)
    private CircleImage mPersonPhoto;
    @ViewInject(R.id.pgemge_name)
    private TextView mPersonName;
    @ViewInject(R.id.pgemge_nick)
    private TextView mPersonNick;
    @ViewInject(R.id.pgemge_sex)
    private TextView mPersonSex;
    @ViewInject(R.id.pgemge_save)
    private Button mPersonSave;

    private String uname;
    private String nick;

    private Bitmap bmp;
    private int type;
    private Integer sex = null;
    private boolean isPhoto = false;

    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());

    private SelectPicPopupWindow menuWindow;

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
//        return sdf.format(date) + ".png";
        return "temp.png";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_personage_message);
        ViewUtils.inject(this);
        if (MyApp.user != null) {
            XUtils.bitmapUtils.display(mPersonPhoto, XUtils.BURL + MyApp.user.getPhotoUrl());
            mPersonName.setText(MyApp.user.getUname() == null ? "未填写" : MyApp.user.getUname());
            mPersonNick.setText(MyApp.user.getNick() == null ? "未填写" : MyApp.user.getNick());
            mPersonSex.setText(MyApp.user.getGender() == 0 ? "女" : "男");
        }
        if (null != bmp) {
            mPersonPhoto.setImageBitmap(bmp);
        }
        if (!TextUtils.isEmpty(uname)) {
            mPersonName.setText(uname);
        }
        if (!TextUtils.isEmpty(nick)) {
            mPersonNick.setText(nick);
        }
        if (sex != null) {
            if (sex == 1) {
                mPersonSex.setText("男");
            } else {
                mPersonSex.setText("女");
            }
        }

    }

    @OnClick({R.id.pgemge_photo, R.id.pgemge_save, R.id.title_left, R.id.pgemge_sex, R.id.btn_top, R.id.btn_bottom, R.id.pgemge_rl_name, R.id.pgemge_rl_nick})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                //返回上一层
                finish();
                endIntentAnim();
                break;
            case R.id.pgemge_photo:
                type = MyApp.PHOTO;
                //更改头像
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(PersonageMessageActivity.this, itemsOnClick, MyApp.PHOTO);
                //显示窗口
                menuWindow.showAtLocation(PersonageMessageActivity.this.findViewById(R.id.pgemge_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.pgemge_sex:
                type = MyApp.SEX;
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(PersonageMessageActivity.this, itemsOnClick, MyApp.SEX);
                //显示窗口
                menuWindow.showAtLocation(PersonageMessageActivity.this.findViewById(R.id.pgemge_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.pgemge_save:
                //保存
                updatePersonInfo();
                break;
            case R.id.pgemge_rl_name:
                EditDialog.showDialog(this, "请输入新姓名");
                EditDialog.setButtonEnsure(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = EditDialog.getText();
                        mPersonName.setText(name);
                        EditDialog.hiddenWaitting();
                    }
                });
                break;
            case R.id.pgemge_rl_nick:
                EditDialog.showDialog(this, "请输入新昵称");
                EditDialog.setButtonEnsure(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nick = EditDialog.getText();
                        mPersonNick.setText(nick);
                        EditDialog.hiddenWaitting();
                    }
                });
                break;
        }
    }

    private void updatePersonInfo() {
        if (checkInfo()) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("u.uid", String.valueOf(MyApp.user.getUid()));
            if (isPhoto) {
                params.addBodyParameter("photo", tempFile);
            }
            params.addBodyParameter("u.uname", uname);
            params.addBodyParameter("u.nick", nick);
            params.addBodyParameter("u.gender", String.valueOf(sex));
            DialogUtil.showWaitting(this);
            XUtils.send(XUtils.UPHOTO, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    DialogUtil.hiddenWaitting();
                    if (null != responseInfo) {
                        JsonUtil<Result<Muser>> jsonUtil = new JsonUtil<Result<Muser>>(new TypeReference<Result<Muser>>() {
                        });
                        Result<Muser> result = jsonUtil.parse(responseInfo.result);
                        XUtils.showToast(result.desc);
                        if (result.state == Result.STATE_SUC) {
                            MyApp.user = result.data;
                            uname = null;
                            nick = null;
                            sex = null;
                            finish();
                            endIntentAnim();
                        }
                    } else {
                        XUtils.showToast("发生错误");
                    }
                }
            });
        }
    }

    private boolean checkInfo() {
        uname = mPersonName.getText().toString().trim();
        nick = mPersonNick.getText().toString().trim();
        String gender = mPersonSex.getText().toString().trim();
        if (gender.equals("男")) {
            sex = 1;
        } else if (gender.equals("女")) {
            sex = 0;
        }
        if (TextUtils.isEmpty(uname)) {
            XUtils.showToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(nick)) {
            XUtils.showToast("请输入昵称");
            return false;
        }
        return true;
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            uname = mPersonName.getText().toString().trim();
            nick = mPersonNick.getText().toString().trim();
            switch (v.getId()) {
                case R.id.btn_top:
                    if (type == MyApp.PHOTO) {
                        startCamera();
                    } else if (type == MyApp.SEX) {
                        mPersonSex.setText("男");
                        sex = 1;
                    }
                    break;
                case R.id.btn_bottom:
                    if (type == MyApp.PHOTO) {
                        startPick();
                    } else if (type == MyApp.SEX) {
                        mPersonSex.setText("女");
                        sex = 0;
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private void startPick() {
        menuWindow.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    private void startCamera() {
        menuWindow.dismiss();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, PHOTO_CARMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CARMERA:
                startPhotoZoom(Uri.fromFile(tempFile), 300);
                break;
            case PHOTO_PICK:
                if (null != data) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case PHOTO_CUT:
                if (null != data) {
                    setPicToView(data);
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统剪裁
     *
     * @param uri
     * @param size
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }

    /**
     * 设置图片显示在界面
     *
     * @param data
     */
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            bmp = bundle.getParcelable("data");
            saveCropPic(bmp);
        }
    }

    /**
     * 把剪裁完的图片保存到手机sdcard中
     *
     * @param bmp
     */
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            isPhoto = true;
            if (tempFile.exists()) {
                tempFile.delete();
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        getPhotoFileName());
            }
            fis = new FileOutputStream(tempFile);
            fis.write(baos.toByteArray());
            fis.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
