package com.stuonline.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuonline.R;
import com.stuonline.views.CircleRefreshLayout;

/**
 * Created by Xubin on 2015/9/24.
 */
public class ImageFragment extends Fragment {

    private View view;
    private CircleRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view=inflater.inflate(R.layout.fragment_image,null);
            refreshLayout= (CircleRefreshLayout) view.findViewById(R.id.fragment_img_refresh);
            refreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
                @Override
                public void completeRefresh() {

                }

                @Override
                public void refreshing() {

                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 更新完后调用该方法结束刷新
                            refreshLayout.finishRefreshing();
                        }
                    }, 2000);
                }
            });
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        return view;
    }
}
