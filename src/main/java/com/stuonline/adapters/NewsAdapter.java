package com.stuonline.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuonline.R;
import com.stuonline.entity.News;
import com.stuonline.https.XUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xubin on 2015/9/21.
 */
public class NewsAdapter extends BaseAdapter {

    private List<News> newsList;
    private LayoutInflater lif;

    public NewsAdapter(Context context, List<News> newsList) {
        if (null != newsList) {
            this.newsList = newsList;
        } else {
            this.newsList = new ArrayList<>();
        }
        this.lif = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public News getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).getNid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            convertView=lif.inflate(R.layout.layout_lv_item,null);
            holder=new ViewHolder();
            holder.vp= (ViewPager) convertView.findViewById(R.id.news_item_vp);
            holder.thumb= (ImageView) convertView.findViewById(R.id.news_item_thumb);
            holder.title= (TextView) convertView.findViewById(R.id.news_item_title);
            holder.resume= (TextView) convertView.findViewById(R.id.news_item_resume);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        News news=getItem(position);
        XUtils.bitmapUtils.display(holder.thumb,XUtils.BURL+news.getNewsUrl());
        holder.title.setText(news.getTitle());
        holder.resume.setText(news.getResume());
        return convertView;
    }

    public void clear(){
        this.newsList.clear();
    }

    public void addAll(List<News> newsList){
        if (null != newsList){
            this.newsList.addAll(newsList);
            notifyDataSetChanged();
        }
    }

    class ViewHolder{
        ViewPager vp;
        ImageView thumb;
        TextView title;
        TextView resume;
    }
}
