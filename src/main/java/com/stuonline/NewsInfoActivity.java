package com.stuonline;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.adapters.CommentAdapter;
import com.stuonline.entity.Comment;
import com.stuonline.entity.News;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.views.TitleView;

import java.util.List;

/**
 * Created by Xubin on 2015/9/22.
 */
public class NewsInfoActivity extends BaseActivity {

    private int nid;
    @ViewInject(R.id.news_info_title)
    private TitleView titleView;
    private WebView webView;
    private ProgressBar pb;
    @ViewInject(R.id.news_info_lv)
    private PullToRefreshListView lv;
    @ViewInject(R.id.news_info_share)
    private ImageView newsInfoShare;
    private Integer pageIndex=1;

    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nid=getIntent().getIntExtra("nid",-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        setContentView(R.layout.activity_news_info);
        ViewUtils.inject(this);
        titleView.setOnLeftclickListener(leftClis);
        lv.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
        lv.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        loadHeader();
        loadData(false);
        adapter=new CommentAdapter(this,null);
        lv.setAdapter(adapter);
        lv.setOnRefreshListener(listener2);

    }
    @OnClick(R.id.news_info_share)
    private  void onClick(View v){

    }

    private PullToRefreshBase.OnRefreshListener2 listener2=new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                pageIndex=1;
                loadData(true);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            loadData(false);
        }
    };

    /**
     * 加载评论列表
     * @param isFlush
     */
    private void loadData(final boolean isFlush) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
        params.addBodyParameter("pageSize", String.valueOf(5));
        params.addBodyParameter("nid", String.valueOf(nid));
        httpHandler=XUtils.send(XUtils.QUERYCOMMENTS, params, new MyCallBack<Result<List<Comment>>>(new TypeReference<Result<List<Comment>>>() {
        }) {
            @Override
            public void success(Result<List<Comment>> result) {
                lv.onRefreshComplete();
                if (isFlush) {
                    adapter.clear();
                }
                adapter.addAll(result.data);
                pageIndex++;
            }

            @Override
            public void failure() {
                lv.onRefreshComplete();
                super.failure();
            }
        }, true);
    }

    /**
     * 加载头部
     */
    private void loadHeader() {
        View header=getLayoutInflater().inflate(R.layout.layout_news_info_header,null);
        webView= (WebView) header.findViewById(R.id.news_info_header_wv);
        pb= (ProgressBar) header.findViewById(R.id.news_info_header_web_pb);
        // 网页加载设置
        WebSettings settings=webView.getSettings();
        // 设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(XUtils.BURL+XUtils.QUERYNEWS+"?nid="+nid);
        // 设置Chrome客户端，主要操作内容
        webView.setWebChromeClient(webChromeClient);
        lv.getRefreshableView().addHeaderView(header);
    }


    private View.OnClickListener leftClis=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            endIntentAnim();
        }
    };

    private WebChromeClient webChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view,newProgress);
            pb.setProgress(newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
        }
    };
}
