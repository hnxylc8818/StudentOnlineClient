package com.stuonline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stuonline.NewsInfoActivity;
import com.stuonline.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Xubin on 2015/9/22.
 */
public class ADAdapter extends PagerAdapter {

    private List<Map<String, Object>> data;
    private Activity activity;

    public ADAdapter(Activity activity,List<Map<String, Object>> data){
        if (data == null){
            data=new ArrayList<>();
        }else{
            this.data=data;
        }
        this.activity=activity;
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
                Intent intent =new Intent(activity, NewsInfoActivity.class);
                intent.putExtra("nid", (Integer) data.get(position).get("nid"));
                activity.startActivity(intent);
                startIntentAnim();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    protected void startIntentAnim() {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
