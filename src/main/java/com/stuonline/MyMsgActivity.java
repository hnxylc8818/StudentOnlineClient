package com.stuonline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stuonline.adapters.CommentAdapter;
import com.stuonline.entity.Comment;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.views.TitleView;

import java.util.List;

/**
 * Created by Xubin on 2015/9/24.
 */
public class MyMsgActivity extends BaseActivity {
    @ViewInject(R.id.my_message_title)
    private TitleView title;
    @ViewInject(R.id.my_message_lv)
    private PullToRefreshListView lv;
    private Integer pageIndex = 1;
    private CommentAdapter adapter;
    private boolean isSF = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        setContentView(R.layout.activity_my_message);
        ViewUtils.inject(this);
        title.setOnLeftclickListener(leftClis);
        lv.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
        lv.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
        lv.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        lv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");
        pageIndex=1;
        loadData(true);
        adapter = new CommentAdapter(this, null);
        lv.setAdapter(adapter);
        lv.setOnRefreshListener(listener2);
        lv.setOnItemClickListener(itemClickListener);
    }

    private AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int nid=adapter.getNid(position);
            Intent intent=new Intent(MyMsgActivity.this, NewsInfoActivity.class);
            intent.putExtra("nid",nid);
            startActivity(intent);
            startIntentAnim();
        }
    };

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

    /**
     * 加载评论列表
     *
     * @param isFlush
     */
    private void loadData(final boolean isFlush) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
        params.addBodyParameter("pageSize", String.valueOf(5));
        params.addBodyParameter("uid", String.valueOf(MyApp.user.getUid()));
        httpHandler = XUtils.send(XUtils.QUCOMMENTBUID, params, new MyCallBack<Result<List<Comment>>>(new TypeReference<Result<List<Comment>>>() {
        }) {
            @Override
            public void success(Result<List<Comment>> result) {
                lv.onRefreshComplete();

                if (pageIndex == 1 && (result.data == null || result.data.size() == 0)) {
                    Comment comment = new Comment();
                    Muser muser = new Muser();
                    muser.setPhotoUrl("images/default.png");
                    comment.setMuser(muser);
                    comment.setContent("您还没有消息，快去评论吧！");
                    adapter.clear();
                    adapter.addAll(comment);
                    isSF = true;
                } else {
                    if (isFlush || isSF) {
                        if (isSF) {
                            isSF = false;
                        }
                        adapter.clear();
                    }
                    adapter.addAll(result.data);
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

    private View.OnClickListener leftClis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            endIntentAnim();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}

