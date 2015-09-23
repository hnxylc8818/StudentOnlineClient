package com.stuonline;

import android.content.Intent;
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
import com.stuonline.views.TitleView;

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
    private String istrue="1";
    private String isfalse="0";
    private TitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_add_tab);
        titleView= (TitleView) findViewById(R.id.add_title);
        titleView.setOnLeftclickListener(onclicllistenr);
        initView();
        dbTools=new DBTools(this);
        if (dbTools.queryAll()!=null && dbTools.queryAll().size()>0){
            strList=new ArrayList<>();
            strListAll=new ArrayList<>();
            strList = dbTools.queryAllisMe(istrue);
            MyApp.setMeTabs(strList);
            adapter.AddAll(strList);
            strListAll=dbTools.queryAllisMe(isfalse);
            adapter2.AddAll(strListAll);
        }else {
            dbTools.deleteTab();
            initData();
        }



    }
    public View.OnClickListener onclicllistenr=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_left:
                    if (MyApp.isMainChange){
                        Intent intent=new Intent(AddTabActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    endIntentAnim();
                    break;
            }
        }
    };

    public void initData(){
        strList2 = new ArrayList<Tab>();
        strList=new ArrayList<MeTab>();
        httpHandler=XUtils.send(XUtils.QUERYTABS, null, new MyCallBack<Result<List<Tab>>>(new TypeReference<Result<List<Tab>>>(){}) {

            @Override
            public void success(Result<List<Tab>> result) {
                if (result.state == Result.STATE_SUC) {
                    strList2 = result.data;
                    if (strList2 != null && strList2.size() > 0) {

                        for (int i = 0; i < strList2.size(); i++) {
                            MeTab meTab = new MeTab();
                            meTab.setT_tabname(strList2.get(i).getTab());
                            meTab.setT_isMe(true);
                            dbTools.add(meTab);

                        }
                        strList = dbTools.queryAllisMe(istrue);
                        MyApp.setMeTabs(strList);
                        adapter.AddAll(strList);
                        strListAll=dbTools.queryAllisMe(isfalse);
                        adapter2.AddAll(strListAll);
                    }
                }
            }
        },true);


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
            adapter.clear();
            strList = dbTools.queryAllisMe(istrue);
            MyApp.setMeTabs(strList);
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
            adapter.clear();
            strList = dbTools.queryAllisMe(istrue);
            MyApp.setMeTabs(strList);
            adapter.AddAll(strList);
            adapter2.clear();
            strListAll = dbTools.queryAllisMe(isfalse);
            adapter2.AddAll(strListAll);
            adapter.showHideView();
        }
    };

}
