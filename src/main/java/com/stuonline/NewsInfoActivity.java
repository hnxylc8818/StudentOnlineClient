package com.stuonline;

import android.os.Bundle;
import android.webkit.WebView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stuonline.views.TitleView;

/**
 * Created by Xubin on 2015/9/22.
 */
public class NewsInfoActivity extends BaseActivity {

    private int nid;
    @ViewInject(R.id.news_info_title)
    private TitleView titleView;
    @ViewInject(R.id.news_info_wv)
    private WebView webView;

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
        ViewUtils.inject(this);
    }
}
