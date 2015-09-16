package com.stuonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.SharedUtil;
import com.stuonline.views.CircleImage;
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
    private TextView mPersonSex;
    @ViewInject(R.id.pgemge_save)
    private Button mPersonSave;

    private Bitmap bmp;

    private String[] items = { "拍照", "相册" };
    private String title = "选择照片";
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_personage_message);
        ViewUtils.inject(this);
        if (MyApp.user != null){
            XUtils.bitmapUtils.display(mPersonPhoto,XUtils.BURL+MyApp.user.getPhotoUrl());
            mPersonName.setText(MyApp.user.getUname()==null?"未填写":MyApp.user.getUname());
            mPersonNick.setText(MyApp.user.getNick()==null?"未填写":MyApp.user.getNick());
            mPersonSex.setText(MyApp.user.getGender()==0?"女":"男");
        }
        if (null != bmp){
            mPersonPhoto.setImageBitmap(bmp);
        }
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
                AlertDialog.Builder dialog = DialogUtil.getListDialogBuilder
                        (PersonageMessageActivity.this, items, title, dialogListener);
                dialog.show();
                break;
            case R.id.pgemge_save:
                //保存
                break;


        }
    }
    private DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    // 调用拍照
                    startCamera(dialog);
                    break;
                case 1:
                    // 调用相册
                    startPick(dialog);
                    break;

                default:
                    break;
            }
        }
    };
    private void startPick(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    private void startCamera(DialogInterface dialog) {
        dialog.dismiss();
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
     * @param bmp
     */
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
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
