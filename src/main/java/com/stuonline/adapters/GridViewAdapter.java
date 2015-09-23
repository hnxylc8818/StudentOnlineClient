package com.stuonline.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stuonline.R;
import com.stuonline.entity.MeTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolin on 2015/1/24.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<MeTab> strList;
    private int hidePosition = AdapterView.INVALID_POSITION;

    public GridViewAdapter(Context context, List<MeTab> strList) {
        this.context = context;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        return strList != null ? strList.size() : 0;
    }

    @Override
    public MeTab getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return strList.get(position).getT_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = new TextView(context);
            view.setTextColor(Color.RED);
            view.setBackgroundResource(R.drawable.seletor_item_tab);
            view.setGravity(Gravity.CENTER);
        } else {
            view = (TextView) convertView;
            view.setTextColor(Color.RED);
            view.setBackgroundResource(R.drawable.seletor_item_tab);
            view.setGravity(Gravity.CENTER);
        }

        //hide时隐藏Text
        if (position != hidePosition) {
            view.setText(strList.get(position).getT_tabname());
        } else {
            view.setText("");
        }
        view.setId(position);

        return view;
    }

    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    public void removeView(int pos) {
        strList.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if (draggedPos < destPos) {
            strList.add(destPos + 1, getItem(draggedPos));
            strList.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            strList.add(destPos, getItem(draggedPos));
            strList.remove(draggedPos + 1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }

    public void clear() {
        this.strList.clear();
    }

    public void AddAll(List<MeTab> meTabs) {

        if (null != meTabs) {
            if (this.strList == null) {
                this.strList = new ArrayList<>();
            }
            this.strList.addAll(meTabs);
            notifyDataSetChanged();
        }

    }
}
