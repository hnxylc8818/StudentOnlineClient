package com.stuonline.entity;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MeTab {
    private  int t_id;
    private  String t_tabname;
    private boolean t_isMe;

    public String getT_tabname() {
        return t_tabname;
    }

    public void setT_tabname(String t_tabname) {
        this.t_tabname = t_tabname;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public boolean isT_isMe() {
        return t_isMe;
    }

    public void setT_isMe(boolean t_isMe) {
        this.t_isMe = t_isMe;
    }
}
