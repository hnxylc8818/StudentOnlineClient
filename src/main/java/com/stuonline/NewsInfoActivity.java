package com.stuonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.adapters.CommentAdapter;
import com.stuonline.entity.Comment;
import com.stuonline.entity.Muser;
import com.stuonline.entity.News;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.views.TitleView;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

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
    @ViewInject(R.id.news_info_ping)
    private ImageView img;
    private Integer pageIndex = 1;

    private CommentAdapter adapter;
    private Dialog dialog;
    private EditText etContet;
    private boolean isSF = false;
    private Dialog dialogShrea;
    private News news;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nid = getIntent().getIntExtra("nid", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        setContentView(R.layout.activity_news_info);
        ShareSDK.initSDK(this);
        ViewUtils.inject(this);
        titleView.setOnLeftclickListener(leftClis);
        lv.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
        lv.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        loadHeader();
        loadNews();
        loadData(false);
        adapter = new CommentAdapter(this, null);
        lv.setAdapter(adapter);
        lv.setOnRefreshListener(listener2);

    }


    private PullToRefreshBase.OnRefreshListener2 listener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            pageIndex = 1;
            loadData(true);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            loadData(false);
        }
    };

    @OnClick({R.id.news_info_share, R.id.news_info_ping})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_info_ping:
                if (MyApp.user == null) {
                    Intent intent = new Intent(NewsInfoActivity.this, LoginActivity.class);
                    intent.putExtra("newinfo", 6);
                    startActivity(intent);
                    startIntentAnim();
                    return;
                }
                RemarkInfo();
                break;
            case R.id.news_info_share:
                if (MyApp.user == null) {
                    Intent intent = new Intent(NewsInfoActivity.this, LoginActivity.class);
                    intent.putExtra("newinfo", 6);
                    startActivity(intent);
                    startIntentAnim();
                    return;
                }
                dialogAdd();
                break;
        }
    }

    private void loadNews() {
        RequestParams params=new RequestParams();
        params.addBodyParameter("nid", String.valueOf(nid));
        httpHandler=XUtils.send(XUtils.LOADNEWS, params, new MyCallBack<Result<News>>(new TypeReference<Result<News>>(){}) {

            @Override
            public void success(Result<News> result) {
                news=result.data;
            }
        },true);
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.quez:
                    dialogShrea.dismiss();
                    Platform platformQ = ShareSDK.getPlatform(NewsInfoActivity.this, QZone.NAME);
                    shearsdk(platformQ);
                    break;
                case R.id.sina:
                    dialogShrea.dismiss();
                    Platform platformS = ShareSDK.getPlatform(NewsInfoActivity.this, SinaWeibo.NAME);
                    shearsdk(platformS);
                    break;
            }
        }
    };

    private void dialogAdd() {
        if (dialogShrea == null) {
            View v = LayoutInflater.from(NewsInfoActivity.this).inflate(
                    R.layout.layout_shearsdk, null);
            v.setBackgroundColor(Color.TRANSPARENT);
            TextView quez = (TextView) v.findViewById(R.id.quez);
            TextView sina = (TextView) v.findViewById(R.id.sina);
            quez.setOnClickListener(clickListener);
            sina.setOnClickListener(clickListener);
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    NewsInfoActivity.this);
            dialogShrea = builder.show();
            dialogShrea.setCanceledOnTouchOutside(true);
            Window w = dialogShrea.getWindow();
            w.setContentView(v);
            w.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            w.setGravity(Gravity.BOTTOM);
        } else {
            dialogShrea.show();
        }
    }

    private void shearsdk(Platform platformList) {
        // 获取已经注册到SDK的平台实例列表
        platformList.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                // 操作成功的处理代码
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                // 操作失败的处理代码
            }

            @Override
            public void onCancel(Platform platform, int i) {
                // 操作取消的处理代码
            }
        });

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(news.getTitle());
        sp.setTitleUrl(XUtils.BURL + XUtils.QUERYNEWS + "?nid=" + nid); // 标题的超链接
        sp.setText(news.getResume());
        sp.setImageUrl(XUtils.BURL+news.getNewsUrl());
        sp.setSite("中国大学生在线");
        sp.setSiteUrl(XUtils.BURL + XUtils.QUERYNEWS + "?nid=" + nid);
        // 执行图文分享
        platformList.share(sp);
    }

    private void RemarkInfo() {
        if (dialog == null) {
            View v = LayoutInflater.from(NewsInfoActivity.this).inflate(
                    R.layout.layout_remar_dialog, null);
            v.setBackgroundColor(Color.WHITE);
            Button btEnsure = (Button) v.findViewById(R.id.remar_dialog_bt);
            etContet = (EditText) v.findViewById(R.id.remar_dialog_edit);
            btEnsure.setOnClickListener(onClickListener);
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    NewsInfoActivity.this);
            dialog = builder.show();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialog.getWindow().setAttributes(lp);
            dialog.setCanceledOnTouchOutside(true);
            Window w = dialog.getWindow();
            w.setContentView(v);
            w.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            w.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
        } else {
            dialog.show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = etContet.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                XUtils.showToast("评论内容不能为空");
                return;
            }
            RequestParams params = new RequestParams();
            params.addBodyParameter("comment.nid", String.valueOf(nid));
            params.addBodyParameter("comment.muser.uid", String.valueOf(MyApp.user.getUid()));
            params.addBodyParameter("comment.content", content);
            httpHandler = XUtils.send(XUtils.SAVECOMMENT, params, new MyCallBack<Result<Boolean>>(new TypeReference<Result<Boolean>>() {
            }) {
                @Override
                public void success(Result<Boolean> result) {
                    XUtils.showToast(result.desc);
                    if (result.data) {
                        dialog.dismiss();
                        loadData(false);
                        etContet.getText().clear();
                    }

                }
            }, true);
        }
    };

    /**
     * 加载评论列表
     *
     * @param isFlush
     */
    private void loadData(final boolean isFlush) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
        params.addBodyParameter("pageSize", String.valueOf(5));
        params.addBodyParameter("nid", String.valueOf(nid));
        httpHandler = XUtils.send(XUtils.QUERYCOMMENTS, params, new MyCallBack<Result<List<Comment>>>(new TypeReference<Result<List<Comment>>>() {
        }) {
            @Override
            public void success(Result<List<Comment>> result) {
                lv.onRefreshComplete();
                if (isFlush || isSF) {
                    if (isSF) {
                        isSF = false;
                    }
                    adapter.clear();
                }
                adapter.addAll(result.data);
                if (pageIndex == 1 && (result.data == null || result.data.size()==0)) {
                    Comment comment = new Comment();
                    Muser muser = new Muser();
                    muser.setPhotoUrl("images/default.png");
                    muser.setNick("还没有人评论");
                    comment.setMuser(muser);
                    comment.setContent("快来抢沙发吧！");
                    adapter.clear();
                    adapter.addAll(comment);
                    isSF = true;
                }else {
                    pageIndex++;
                }

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
        View header = getLayoutInflater().inflate(R.layout.layout_news_info_header, null);
        webView = (WebView) header.findViewById(R.id.news_info_header_wv);
        pb = (ProgressBar) header.findViewById(R.id.news_info_header_web_pb);
        // 网页加载设置
        WebSettings settings = webView.getSettings();
        // 设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        if (MyApp.isNight) {
            webView.setBackgroundColor(Color.parseColor("#FF2B2B2B"));
        } else {
            webView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        webView.loadUrl(XUtils.BURL + XUtils.QUERYNEWS + "?nid=" + nid);
        // 设置Chrome客户端，主要操作内容
        webView.setWebChromeClient(webChromeClient);
        lv.getRefreshableView().addHeaderView(header);
    }


    private View.OnClickListener leftClis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            endIntentAnim();
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setProgress(newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
        }
    };
}
