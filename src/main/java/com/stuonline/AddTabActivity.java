package com.stuonline;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.stuonline.adapters.GridViewAdapter;
import com.stuonline.https.XUtils;
import com.stuonline.views.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class AddTabActivity extends BaseActivity{
    private List<String> strList;
    private GridView gridView;
    private GridView gridViewAll;
    private List<String> strList2;
    GridViewAdapter adapter2;
    GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_add_tab);
        initData();
        initView();
        for (int i=0;i<strList.size();i++){
            for (int j=0;j<strList2.size();j++){
                if (strList.get(i).equals(strList2.get(j))){
                    strList2.remove(j);
                }
            }


        }
    }

    public void initData(){
        strList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            strList.add("Channel " + i);
        }
        strList2 = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            strList2.add("Channel " + i);
        }

    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.drag_grid_views);
        gridViewAll = (GridView) findViewById(R.id.drag_grid_views2);
         adapter = new GridViewAdapter(this, strList);
         adapter2 = new GridViewAdapter(this, strList2);
        gridView.setAdapter(adapter);
        gridViewAll.setAdapter(adapter2);
        gridView.setOnItemClickListener(listenter);
        gridViewAll.setOnItemClickListener(Lienter);
    }
    private GridView.OnItemClickListener listenter=new GridView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            strList2.add(strList.get(position));
            adapter.removeView(position);
            adapter2.showHideView();

        }
    };
    private GridView.OnItemClickListener Lienter=new GridView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            strList.add(strList2.get(position));
            adapter2.removeView(position);
            adapter.showHideView();
        }
    };

}
