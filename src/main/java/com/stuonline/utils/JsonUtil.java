package com.stuonline.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.stuonline.https.XUtils;

import java.lang.reflect.Type;

/**
 * Created by Xubin on 2015/9/2.
 */
public class JsonUtil<T> {
    private Type mType;
    private TypeReference<T> mReference;

    public JsonUtil(Type mType) {
        this.mType = mType;
    }

    public JsonUtil(TypeReference<T> mReference) {
        this.mReference = mReference;
    }

    public T parse(String json){
        T t=null;
        if (!TextUtils.isEmpty(json)){
            if (json.matches("^\\{.*\\}$")) {
                if (null != mType) {
                    t= JSON.parseObject(json, mType);
                }else if (null != mReference) {
                    t=JSON.parseObject(json,mReference);
                }
                return t;
            }else if (json.matches("^\\[\\{.*\\}\\]$")) {
                t=JSON.parseObject(json,mReference);
                return t;
            }else{
                XUtils.showToast("json格式不匹配");
            }
        }else{
            XUtils.showToast("json为null或''");
        }
        return t;
    }
}
