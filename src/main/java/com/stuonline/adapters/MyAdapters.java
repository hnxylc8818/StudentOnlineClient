package com.stuonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.stuonline.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/20.
 */
public class MyAdapters extends BaseAdapter{
    private List<PoiInfo> poiInfos;
    private LayoutInflater lif;
    public MyAdapters(Context context,List<PoiInfo> poiInfos){
        this.lif=LayoutInflater.from(context);
        if(poiInfos!=null){
            this.poiInfos = poiInfos;
        }else{
            this.poiInfos  = new ArrayList<>();
        }
    }
    @Override
    public int getCount() {
        return poiInfos!=null?poiInfos.size():0;
    }

    @Override
    public PoiInfo getItem(int position) {
        return poiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if (convertView ==null) {
            holder =new ViewHolder();
            convertView =lif.inflate(R.layout.layout_item_shool,null);
            holder.name =(TextView)convertView.findViewById(R.id.item_name);
            holder.adress =(TextView)convertView.findViewById(R.id.item_adsses);
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder)convertView.getTag();
        }
        PoiInfo poiInfo=getItem(position);
        holder.name.setText(poiInfo.name);
        holder.adress.setText(poiInfo.address);
        return convertView;
    }
class ViewHolder{
    TextView name;
    TextView adress;


}
    //清除方法
    public void clear(){
        this.poiInfos.clear();
        //刷新
        notifyDataSetChanged();
    }


    public void addAll(List<PoiInfo> data){
        this.poiInfos.addAll(data);
        notifyDataSetChanged();
    }
}
