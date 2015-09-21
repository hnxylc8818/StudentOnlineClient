package com.stuonline.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.stuonline.R;
import com.stuonline.adapters.NewsAdapter;
import com.stuonline.entity.News;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;

import java.util.List;

/**
 * Created by Xubin on 2015/9/20.
 */
public class ItemFragment extends Fragment {


    private View view;
    private List<News> newsList;
    private Integer pageIndex;
    private PullToRefreshListView lv;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_item, container, false);
            lv= (PullToRefreshListView) view.findViewById(R.id.news_lv);
            lv.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
            //获取Activity传递过来的参数
            Bundle mBundle = getArguments();
            String tid=mBundle.getString("tid");
            loadNews(tid);
        }
        ViewGroup parent= (ViewGroup) view.getParent();
        if (null != parent){
            parent.removeView(view);
        }
        return view;
    }

    private void loadNews(String tid) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("pageSize", String.valueOf(5));
        params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
        params.addBodyParameter("tid", String.valueOf(tid));
        DialogUtil.showWaitting(getActivity());
        XUtils.send(XUtils.QUERYNEWSES, params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                DialogUtil.hiddenWaitting();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                DialogUtil.hiddenWaitting();
                XUtils.showToast("网络连接错误");
                Log.e("MainActivity", "====error======" + s);
                e.printStackTrace();
            }
        });
    }
}
