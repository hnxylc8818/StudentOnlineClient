package com.stuonline.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.stuonline.MyApp;
import com.stuonline.R;
import com.stuonline.adapters.NewsAdapter;
import com.stuonline.entity.News;
import com.stuonline.entity.Result;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;

import java.io.FileInputStream;
import java.util.List;

/**
 * Created by Xubin on 2015/9/20.
 */
public class ItemFragment extends Fragment {


    private View view;
    private Integer pageIndex = 1;
    private PullToRefreshListView lv;
    private NewsAdapter adapter;
    private String tid;
    private ViewPager vp;
    private List<News> newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_item, container, false);
            lv = (PullToRefreshListView) view.findViewById(R.id.news_lv);
            View header = inflater.inflate(R.layout.layout_news_lv_header, null);
            vp = (ViewPager) header.findViewById(R.id.news_header_vp);
            lv.getRefreshableView().addHeaderView(header);
            lv.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
            lv.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
            adapter = new NewsAdapter(getActivity(), null);
            lv.setAdapter(adapter);
            lv.setOnRefreshListener(listener2);
            //获取Activity传递过来的参数
            Bundle mBundle = getArguments();
            tid = mBundle.getString("tid");
            loadNews(false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        return view;
    }

    private void loadHeaderVp(List<News> newsList) {
        Bitmap[] bitmaps = new Bitmap[3];
        Bitmap bmp=null;
        if (newsList != null && newsList.size() > 0) {
            for (int i = 0; i < 3; i++) {
                News news = newsList.get(i);
                Log.i("----news--------",news+"");
                bmp=XUtils.returnBitMap(XUtils.BURL+news.getNewsUrl());
                bitmaps[i] = bmp;
            }
        }
        Log.i("bitmap----------", bitmaps[0] + "");
    }

    private PullToRefreshBase.OnRefreshListener2 listener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            pageIndex = 1;
            loadNews(true);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            loadNews(false);
        }
    };

    private void loadNews(final boolean isFlush) {
        if (pageIndex == null || pageIndex.equals("")) {
            pageIndex = 1;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageSize", String.valueOf(5));
        params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
        params.addBodyParameter("tid", String.valueOf(tid));
        if (!MyApp.isWelcome) {
            DialogUtil.showWaitting(getActivity());
        }
        XUtils.send(XUtils.QUERYNEWSES, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DialogUtil.hiddenWaitting();
                lv.onRefreshComplete();
                if (null != responseInfo) {
                    JsonUtil<Result<List<News>>> jsonUtil = new JsonUtil<Result<List<News>>>(new TypeReference<Result<List<News>>>() {
                    });
                    Result<List<News>> result = jsonUtil.parse(responseInfo.result);
                    if (result.state == Result.STATE_SUC) {
                        if (isFlush) {
                            adapter.clear();
                        }
                        adapter.addAll(result.data);
                        pageIndex++;
                    }
                } else {
                    XUtils.showToast("发生错误");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                DialogUtil.hiddenWaitting();
                lv.onRefreshComplete();
                XUtils.showToast("网络连接错误");
                Log.e("MainActivity", "====error======" + s);
                e.printStackTrace();
            }
        });
    }
}
