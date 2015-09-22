package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;


import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stuonline.adapters.CityAdapter;
import com.stuonline.adapters.MyAdapters;

import com.stuonline.entity.City;
import com.stuonline.utils.DialogUtil;
import com.stuonline.views.TitleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
    @ViewInject(R.id.city_lv)
    private ListView lvCity;
    private String mCity = "北京";
    private CityAdapter cityAdapter;

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
        cityAdapter = new CityAdapter(this, null);
        lv.setAdapter(adapter);
        lvCity.setAdapter(cityAdapter);
        etInput.addTextChangedListener(textWatcher);
        lv.setOnItemClickListener(listener);
        lvCity.setOnItemClickListener(onItemClickListener);
        LoadJson();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DialogUtil.showWaitting(SchoolSearchActivity.this);
            TextView tvCity = (TextView) view.findViewById(R.id.item_city);
            mCity = tvCity.getText().toString();
            poiSearch = PoiSearch.newInstance();
            PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
            citySearchOption.city(mCity);
            citySearchOption.keyword("学校");
            citySearchOption.pageNum(8);
            poiSearch.searchInCity(citySearchOption);
            poiSearch.setOnGetPoiSearchResultListener(getPoiSearchResultListener);
        }
    };
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
            DialogUtil.showWaitting(SchoolSearchActivity.this);
            poiSearch = PoiSearch.newInstance();
            PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
            citySearchOption.city(mCity);
            citySearchOption.keyword(s.toString() + "学校");
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
            DialogUtil.hiddenWaitting();
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult arg0) {

        }
    };

    private void LoadJson() {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(getAssets().open(
                    "city.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            String json = stringBuilder.toString();
            Log.i("TAG", json);
            List<City> data = JSON.parseObject(json, new TypeReference<List<City>>() {
            });
            for (City c : data) {
                cityAdapter.addAll(data);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}