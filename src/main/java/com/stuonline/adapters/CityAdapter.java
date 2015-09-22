package com.stuonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stuonline.R;
import com.stuonline.entity.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunJiShuang on 2015/9/22.
 */
public class CityAdapter extends BaseAdapter {
    private List<City> citys;
    private LayoutInflater lif;

    public CityAdapter(Context context, List<City> citys) {
        this.lif = LayoutInflater.from(context);
        if (citys != null) {
            this.citys = citys;
        } else {
            this.citys = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return citys != null ? citys.size() : 0;
    }

    @Override
    public City getItem(int position) {
        return citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            holder = new ViewHolder();
            v = lif.inflate(R.layout.layout_city_item, null);
            holder.tvCity = (TextView) v.findViewById(R.id.item_city);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        City city = getItem(position);
        holder.tvCity.setText(city.getName());
        return v;
    }

    class ViewHolder {
        TextView tvCity;
    }

    //清除方法
    public void clear() {
        this.citys.clear();
        //刷新
        notifyDataSetChanged();
    }


    public void addAll(List<City> data) {
        this.citys.addAll(data);
        notifyDataSetChanged();
    }
}
