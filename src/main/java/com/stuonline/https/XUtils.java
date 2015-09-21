package com.stuonline.https;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.stuonline.R;
import com.stuonline.dialog.SpotsDialog;

import java.io.File;

/**
 * Created by Xubin on 2015/9/8.
 */
public class XUtils {

    public static final String BURL = "http://123.56.126.25:8080/StuentOnlineServer/";
    //            public static final String BURL = "http://192.168.11.178:8080/StuentOnlineServer/";
    public static final String LOGIN = "login";
    public static final String REG = "reg";
    public static final String UPHOTO = "updatePhoto";
    public static final String UUSER = "updateUser";
    public static final String VER = "ver";
    public static final String QROLE = "queryRole";
    public static final String SFB = "saveFeedBack";
    public static final String QUACCOUNT = "quaccount";
    public static final String SMAIL = "sendMail";
    public static final String REGTHIRD = "regThird";
    public static final String QUERYTABS = "queryTabs";
    public static final String QUERYNEWSES = "queryNewses";
    public static final String QUERYNEWS = "queryNews";
    public static final String QUERYCOMMENTS = "queryComments";
    public static final String SAVECOMMENT = "saveComment";


    public static HttpUtils httpUtils;
    public static BitmapUtils bitmapUtils;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        if (httpUtils == null) {
            httpUtils = new HttpUtils(10000);
        }
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context);
        }
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.load_fail);
        bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
        bitmapUtils.configDefaultReadTimeout(10000);
    }

    public static void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }

    public static HttpHandler send(String url, RequestParams params, RequestCallBack callBack) {
        return httpUtils.send(HttpRequest.HttpMethod.POST, BURL + url, params, callBack);
    }

    public static void download(String url, RequestCallBack<File> callBack) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "StudentOnline"
                + File.separator + "download" + File.separator + "studentonline.apk";
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        httpUtils.download(BURL + url, path, true, true, callBack);
    }
}
