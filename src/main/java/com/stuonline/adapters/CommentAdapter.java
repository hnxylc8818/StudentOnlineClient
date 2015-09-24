package com.stuonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuonline.R;
import com.stuonline.entity.Comment;
import com.stuonline.entity.Muser;
import com.stuonline.https.XUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xubin on 2015/9/23.
 */
public class CommentAdapter extends BaseAdapter {

    private List<Comment> commentList;
    private LayoutInflater lif;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.lif = LayoutInflater.from(context);
        if (commentList == null) {
            this.commentList = new ArrayList<>();
        } else {
            this.commentList = commentList;
        }
    }

    @Override
    public int getCount() {
        return commentList == null ? 0 : commentList.size();
    }

    @Override
    public Comment getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return commentList.get(position).getCid();
    }

    public int getUid(int position) {
        return commentList.get(position).getMuser().getUid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = lif.inflate(R.layout.layout_comment_item, null);
            holder = new ViewHolder();
            holder.photo = (ImageView) convertView.findViewById(R.id.comment_item_photo);
            holder.nick = (TextView) convertView.findViewById(R.id.comment_item_nick);
            holder.content = (TextView) convertView.findViewById(R.id.comment_item_content);
            holder.date = (TextView) convertView.findViewById(R.id.comment_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Comment comment = getItem(position);
        Muser muser = comment.getMuser();
        if (null != muser) {
            XUtils.bitmapUtils.display(holder.photo, XUtils.BURL + muser.getPhotoUrl());
            holder.nick.setText(muser.getNick() == null ? "游客" : muser.getNick());
        }
        holder.content.setText(comment.getContent());
        holder.date.setText(comment.getCdate());
        return convertView;
    }

    public void clear() {
        this.commentList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Comment> comments) {
        if (null != comments) {
            this.commentList.addAll(comments);
            notifyDataSetChanged();
        }
    }
    public void addAll(Comment comment){
        if (null != comment){
            this.commentList.add(comment);
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        ImageView photo;
        TextView nick;
        TextView content;
        TextView date;
    }

}
