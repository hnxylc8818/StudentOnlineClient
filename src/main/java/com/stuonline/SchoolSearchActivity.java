package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;


import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
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

    @ViewInject(R.id.address_input)
    private EditText etInput;
    private PoiSearch poiSearch;
    private MyAdapters adapter;
    @ViewInject(R.id.address_lv)
    private ListView lv;
    @ViewInject(R.id.shools_title)
    private TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_school_address);
        ViewUtils.inject(this);
        title.setOnLeftclickListener(onClickListener);
        adapter = new MyAdapters(this, null);
        lv.setAdapter(adapter);
        etInput.addTextChangedListener(textWatcher);
        lv.setOnItemClickListener(listener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tvAdd = (TextView) view.findViewById(R.id.item_name);
            Intent intent = new Intent();
            intent.putExtra("address", tvAdd.getText().toString());
            setResult(30, intent);
            finish();
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            poiSearch = PoiSearch.newInstance();
            PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
            citySearchOption.city("北京");
            citySearchOption.keyword(s.toString());
            citySearchOption.pageNum(8);
            poiSearch.searchInCity(citySearchOption);
            poiSearch.setOnGetPoiSearchResultListener(getPoiSearchResultListener);
        }
    };

    private OnGetPoiSearchResultListener getPoiSearchResultListener = new OnGetPoiSearchResultListener() {

        @Override
        public void onGetPoiResult(PoiResult result) {
            adapter.clear();
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                return;
            }
            List<PoiInfo> infos = result.getAllPoi();
            if (infos != null && infos.size() > 0) {
                adapter.addAll(infos);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult arg0) {

        }
    };
}