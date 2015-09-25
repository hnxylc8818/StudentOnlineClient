package com.stuonline.https;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
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
import com.stuonline.utils.DialogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Xubin on 2015/9/8.
 */
public class XUtils {

    public static final String BURL = "http://123.56.126.25:8080/StuentOnlineServer/";
    //    public static final String BURL = "http://192.168.11.178:8080/StuentOnlineServer/";
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
    public static final String LOADNEWS = "loadNews";
    public static final String QUERYCOMMENTS = "queryComments";
    public static final String SAVECOMMENT = "saveComment";
    public static final String QUBUID = "queryUserById";
    public static final String QUCOMMENTBUID = "queryCommentsByUid";

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

    public static HttpHandler send(String url, RequestParams params, RequestCallBack callBack, boolean loading) {
        if (loading) {
            DialogUtil.showWaitting();
        }
        return httpUtils.send(HttpRequest.HttpMethod.POST, BURL + url, params, callBack);
    }

    public static HttpHandler download(String url, RequestCallBack<File> callBack) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "StudentOnline"
                + File.separator + "download" + File.separator + "studentonline.apk";
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        return httpUtils.download(BURL + url, path, true, true, callBack);
    }

    public final static Bitmap returnBitMap(final Handler handler, final String url, final int nid) {

        new Thread() {
            @Override
            public void run() {
                URL myFileUrl = null;
                Bitmap bitmap = null;
                try {
                    myFileUrl = new URL(url);
                    HttpURLConnection conn;
                    conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    handler.sendMessage(handler.obtainMessage(1, nid, 0, bitmap));
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        return null;
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (bitmapUtils != null)
            bitmapUtils = null;
        if (httpUtils != null)
            httpUtils = null;
        if (mContext != null)
            mContext = null;
    }

    /**
     * 是否是WIFI网络
     *
     * @return
     */
    public static boolean isWifiConn() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null){
            showToast("当前没有网络");
            return false;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        showToast("为避免流量损失，请在WIFI环境下更新");
        return false;
    }
}
