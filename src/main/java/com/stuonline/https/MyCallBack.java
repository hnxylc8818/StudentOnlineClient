package com.stuonline.https;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;

/**
 * Created by Xubin on 2015/9/8.
 */
public abstract class MyCallBack<T> extends RequestCallBack<String> {

    private static final String JSON_OBJECT = "^\\{.*\\}$";

    private TypeReference<T> reference;

    public MyCallBack(TypeReference<T> reference) {
        this.reference = reference;
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        DialogUtil.hiddenWaitting();
        if (responseInfo != null) {
            String result = responseInfo.result;
            if (result.matches(JSON_OBJECT)) {
                T t = JSON.parseObject(result, reference);
                success(t);
            } else {
                XUtils.showToast("网络请求数据为空");
            }
        }
    }

    @Override
    public void onFailure(HttpException e, String s) {
        DialogUtil.hiddenWaitting();
        failure();
        XUtils.showToast("网络连接错误");
        Log.e("MainActivity", "====error======" + s);
        e.printStackTrace();
    }

    public abstract void success(T result);

    public void failure() {
    }
}
