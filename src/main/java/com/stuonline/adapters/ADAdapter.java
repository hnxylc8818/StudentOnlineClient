package com.stuonline.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Xubin on 2015/9/22.
 */
public class ADAdapter extends PagerAdapter {

    private List<Map<String, Object>> data;

    public ADAdapter(List<Map<String, Object>> data){
        if (data == null){
            data=new ArrayList<>();
        }else{
            this.data=data;
        }
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view=(View) data.get(position).get("view");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("-----nid-------",data.get(position).get("nid")+"");
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
