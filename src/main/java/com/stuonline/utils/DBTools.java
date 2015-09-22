package com.stuonline.utils;

import java.util.ArrayList;
import java.util.List;

import com.stuonline.entity.MeTab;
import com.stuonline.https.XUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;

public class DBTools {

    private DBHelper helper;
    private static String name = "constans";
    private static int banbei = 1;
    private SQLiteDatabase db;
    private static final String table = "metab";

    public DBTools(Context context) {
        helper = new DBHelper(context, name, banbei);
    }

    public List<MeTab> queryAll() {
        List<MeTab> meTabs = new ArrayList<MeTab>();
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(table, new String[]{"t_id", "t_tabname", "t_isMe"}, null, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            MeTab meTab = tabcreat(cursor);
            meTabs.add(meTab);
        }
        cursor.close();
        db.close();
        return meTabs;
    }
    public List<MeTab> queryAllisMe(String tabisme) {
        List<MeTab> meTabs = new ArrayList<MeTab>();
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(table, new String[]{"t_id", "t_tabname", "t_isMe"}, "t_isMe=?", new String []{""+tabisme}, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            MeTab meTab = tabcreat(cursor);
            meTabs.add(meTab);
        }
        cursor.close();
        db.close();
        return meTabs;
    }

    private MeTab tabcreat(Cursor cursor) {
        MeTab meTab = new MeTab();

        int id = cursor.getInt(cursor.getColumnIndex("t_id"));
        String name = cursor.getString(cursor.getColumnIndex("t_tabname"));
        int isme = cursor.getInt(cursor.getColumnIndex("t_isMe"));

        meTab.setT_id(id);
        meTab.setT_tabname(name);
        if (isme == 1) {
            meTab.setT_isMe(true);
        } else if (isme == 0) {
            meTab.setT_isMe(false);
        }
        return meTab;
    }

    public int UpdateMetab( MeTab meTab, String tabId) {
        ContentValues values = new ContentValues();
        values.put("t_isMe", meTab.isT_isMe()==true?1:0);
        db = helper.getWritableDatabase();
        int id = db.update(table, values, "t_id=?", new String[]{tabId});
        db.close();
        return id;
    }
    public void add(MeTab meTab) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("t_tabname", meTab.getT_tabname());
        values.put("t_isMe", meTab.isT_isMe()==true?1:0);
       long id=db.insert(table, null, values);
        if (id==-1) {
            XUtils.showToast("加载失败");
        }
        db.close();

    }
    public void deleteTab(){
        db = helper.getWritableDatabase();
        db.delete(table,null,null);
        db.close();
    }
}
