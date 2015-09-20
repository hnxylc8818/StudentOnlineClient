package com.stuonline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;


import android.widget.ListView;
import android.widget.SearchView;




import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.stuonline.adapters.MyAdapters;

import com.stuonline.https.XUtils;
import com.stuonline.views.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/20.
 */
public class SchoolSearchActivity extends BaseActivity {
    @ViewInject(R.id.shools_title)
    private TitleView mSchoolTitle;
    @ViewInject(R.id.shools_search)
    private SearchView mSchoolSv;
    @ViewInject(R.id.shools_lv)
    private ListView mSchoollv;
    private SuggestionSearch mSearch;
    private List<SuggestionResult.SuggestionInfo> infos;
    private MyAdapters adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        setContentView(R.layout.activity_shools_select);
        ViewUtils.inject(this);
        adapter = new MyAdapters(this, new ArrayList<SuggestionResult.SuggestionInfo>());
        mSchoollv.setAdapter(adapter);
        mSchoollv.setTextFilterEnabled(false);//设置lv可以被过虑

        // 设置该SearchView默认是否自动缩小为图标
        mSchoolSv.setIconifiedByDefault(false);
        // 为该SearchView组件设置事件监听器
        mSchoolSv.setOnQueryTextListener(listener);
        // 设置该SearchView显示搜索按钮
        mSchoolSv.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        mSchoolSv.setQueryHint("查找");
    }

    public SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (TextUtils.isEmpty(query)) {
                // 清除ListView的过滤
                adapter.clear();
//                mSchoollv.clearTextFilter();

            } else {
                XUtils.showToast("aaaaa");
                // 使用用户输入的内容对ListView的列表项进行过滤
                mSearch = SuggestionSearch.newInstance();// 创建搜索对象
                mSearch.setOnGetSuggestionResultListener(listeners);

                mSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(query)
                        .city("北京"));


            }
            return true;

        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {
                // 清除ListView的过滤
                adapter.clear();
//                mSchoollv.clearTextFilter();

            } else {
                XUtils.showToast("aaaaa");
                // 使用用户输入的内容对ListView的列表项进行过滤
                mSearch = SuggestionSearch.newInstance();// 创建搜索对象
                mSearch.setOnGetSuggestionResultListener(listeners);

                mSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(newText)
                        .city("北京"));


            }
            return true;
        }
    };
   public OnGetSuggestionResultListener listeners = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                XUtils.showToast("未搜索到");
                return;
                //未找到相关结果
            }
            //获取在线建议检索结果
            List<SuggestionResult.SuggestionInfo> infos=res.getAllSuggestions();
            adapter.add(infos);

        }
    };



    @OnClick(R.id.title_left)
    public void onClick(View v) {
        finish();
        endIntentAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
    }
}
