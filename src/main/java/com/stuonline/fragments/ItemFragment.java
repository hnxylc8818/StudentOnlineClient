package com.stuonline.fragments;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.stuonline.MyApp;
import com.stuonline.NewsInfoActivity;
import com.stuonline.R;
import com.stuonline.adapters.ADAdapter;
import com.stuonline.adapters.NewsAdapter;
import com.stuonline.entity.News;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xubin on 2015/9/20.
 */
public class ItemFragment extends Fragment {


    private View view;
    private Integer pageIndex = 1;
    private PullToRefreshListView lv;
    private NewsAdapter adapter;
    private ADAdapter adAdapter;
    private String tid;
    private ViewPager vp;
    private int j = 0;
    private Bitmap[] bitmaps = new Bitmap[3];
    private int nid;
    private LinearLayout dd;
    private boolean isLoading=true;
    private List<Map<String, Object>> data = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (j <= 2) {
                        nid = msg.arg1;
                        data.add(getVpData(nid, (Bitmap) msg.obj));
                        if (j == 2) {
                            adAdapter = new ADAdapter(getActivity(),data);
                            vp.setAdapter(adAdapter);
                            loadDD();
                        }
                        j++;
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_item, container, false);
            lv = (PullToRefreshListView) view.findViewById(R.id.news_lv);
            View header = inflater.inflate(R.layout.layout_news_lv_header, null);
            vp = (ViewPager) header.findViewById(R.id.news_header_vp);
            dd = (LinearLayout) header.findViewById(R.id.news_header_dd);
            vp.setOnPageChangeListener(pageChangeListener);
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
            lv.setOnItemClickListener(itemClickListener);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        return view;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getActivity(), NewsInfoActivity.class);
            intent.putExtra("nid",(int)id);
            startActivity(intent);
            startIntentAnim();
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (null != adAdapter && null != dd) {
                // 改变小点选择状态
                for (int i = 0; i < dd.getChildCount(); i++) {
                    if (position == i) {
                        dd.getChildAt(i).setEnabled(true);
                    } else {
                        dd.getChildAt(i).setEnabled(false);
                    }
                }

            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void loadDD() {
        // 设置点点布局属性
        LinearLayout.MarginLayoutParams mlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // 设置点点之间的间距
        int margin = getResources().getDimensionPixelSize(R.dimen.home_header_dd_margin);
        mlp.leftMargin = margin;
        mlp.rightMargin = margin;
        // 设置小点，ViewPager标识器
        for (int i = 0; i < adAdapter.getCount(); i++) {
            // 创建小点图片
            ImageView ddImg = new ImageView(getActivity());
            ddImg.setImageResource(R.drawable.news_header_dd_selector);
            // 通过enable属性设置小点使用的颜色
            if (i == 0) {
                ddImg.setEnabled(true);
            } else {
                ddImg.setEnabled(false);
            }
            // 将小点添加到布局的指定位置
            dd.addView(ddImg, mlp);
        }
    }

    private void loadHeaderVp(List<News> newsList) {
        if (newsList != null && newsList.size() > 0) {
            for (int i = 0; i < 3; i++) {
                News news = newsList.get(i);
                XUtils.returnBitMap(handler, XUtils.BURL + news.getNewsUrl(), news.getNid());
            }
        }
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
            isLoading=false;
        }else{
            isLoading=true;
        }
        XUtils.send(XUtils.QUERYNEWSES, params, new MyCallBack<Result<List<News>>>(new TypeReference<Result<List<News>>>(){}) {

            @Override
            public void success(Result<List<News>> result) {
                lv.onRefreshComplete();
                if (result.state == Result.STATE_SUC) {
                    if (isFlush) {
                        adapter.clear();
                    }
                    loadHeaderVp(result.data);
                    adapter.addAll(result.data);
                    pageIndex++;
                }
            }

            @Override
            public void failure() {
                lv.onRefreshComplete();
                super.failure();

            }
        },isLoading);
    }

    private Map<String, Object> getVpData(int nid, Bitmap bmp) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nid", nid);
        map.put("view", getVpView(bmp));
        return map;
    }

    private ViewPager.LayoutParams lp = null;

    private View getVpView(Bitmap bmp) {
        if (null != bmp) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(bmp);
            if (lp == null) {
                lp = new ViewPager.LayoutParams();
                lp.width = ViewPager.LayoutParams.MATCH_PARENT;
                lp.height = ViewPager.LayoutParams.MATCH_PARENT;
            }
            imageView.setLayoutParams(lp);
            return imageView;
        }
        return null;
    }

    protected void startIntentAnim() {
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
