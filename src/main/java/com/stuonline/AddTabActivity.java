package com.stuonline;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.stuonline.adapters.GridViewAdapter;
import com.stuonline.adapters.GridViewAdapter2;
import com.stuonline.entity.MeTab;
import com.stuonline.entity.Muser;
import com.stuonline.entity.Result;
import com.stuonline.entity.Tab;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DBHelper;
import com.stuonline.utils.DBTools;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;
import com.stuonline.views.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class AddTabActivity extends BaseActivity {
    private List<MeTab> strList;
    private GridView gridView;
    private GridView gridViewAll;
    private List<Tab> strList2;
    private List<MeTab> strListAll;
    GridViewAdapter2 adapter2;
    GridViewAdapter adapter;
    public DBTools dbTools;
    private String istrue = "1";
    private String isfalse = "0";

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
        setContentView(R.layout.activity_add_tab);
        dbTools = new DBTools(this);
        dbTools.deleteTab();
        initData();
        initView();

    }

    public void initData() {
        strList2 = new ArrayList<Tab>();
        strList = new ArrayList<MeTab>();
        DialogUtil.showWaitting(this);
        httpHandler = XUtils.send(XUtils.QUERYTABS, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DialogUtil.hiddenWaitting();
                if (responseInfo != null) {
                    JsonUtil<Result<List<Tab>>> jsonUtil = new JsonUtil<Result<List<Tab>>>(new TypeReference<Result<List<Tab>>>() {
                    });
                    Result<List<Tab>> result = jsonUtil.parse(responseInfo.result);
                    XUtils.showToast(result.desc);
                    if (result.state == Result.STATE_SUC) {
                        strList2 = result.data;
                        Log.i("aaaaa", strList2.size() + "=========================");
                        if (strList2 != null && strList2.size() > 0) {

                            for (int i = 0; i < strList2.size(); i++) {
                                MeTab meTab = new MeTab();
                                meTab.setT_tabname(strList2.get(i).getTab());
                                meTab.setT_isMe(true);
                                dbTools.add(meTab);

                            }
                            strList = dbTools.queryAllisMe(istrue);
                            Log.i("aaaaa", "=======" + strList.size());
                            MyApp.meTabs = strList;
                            adapter.AddAll(strList);
                            XUtils.showToast(strList.size() + "");
                            strListAll = dbTools.queryAllisMe(isfalse);
                            adapter2.AddAll(strListAll);
                        }
                    }
                }
            }
        });


    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.drag_grid_views);
        gridViewAll = (GridView) findViewById(R.id.drag_grid_views2);
        adapter = new GridViewAdapter(this, strList);
        adapter2 = new GridViewAdapter2(this, strListAll);
        gridView.setAdapter(adapter);
        gridViewAll.setAdapter(adapter2);
        gridView.setOnItemClickListener(listenter);
        gridViewAll.setOnItemClickListener(Lienter);
    }

    private GridView.OnItemClickListener listenter = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyApp.isMainChange = true;
            MeTab meTab = new MeTab();
            meTab.setT_tabname(strList.get(position).getT_tabname());
            meTab.setT_isMe(false);
            dbTools.UpdateMetab(meTab, String.valueOf(id));
            adapter.removeView(position);
            adapter.clear();
            strList = dbTools.queryAllisMe(istrue);
            MyApp.meTabs = strList;
            XUtils.showToast(MyApp.meTabs.size() + "");
            adapter.AddAll(strList);
            adapter2.clear();
            strListAll = dbTools.queryAllisMe(isfalse);
            adapter2.AddAll(strListAll);
            adapter.showHideView();

        }
    };
    private GridView.OnItemClickListener Lienter = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyApp.isMainChange = true;
            MeTab meTab = new MeTab();
            meTab.setT_tabname(strListAll.get(position).getT_tabname());
            meTab.setT_isMe(true);
            dbTools.UpdateMetab(meTab, String.valueOf(id));
            adapter.removeView(position);
            adapter.clear();
            strList = dbTools.queryAllisMe(istrue);
            MyApp.meTabs = strList;
            adapter.AddAll(strList);
            XUtils.showToast(MyApp.meTabs.size() + "");
            adapter2.clear();
            strListAll = dbTools.queryAllisMe(isfalse);
            adapter2.AddAll(strListAll);
            adapter.showHideView();
        }
    };

}
