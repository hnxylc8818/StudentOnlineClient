package com.stuonline.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/9/21.
 */
public class DBHelper extends SQLiteOpenHelper {
    private String sqlCreateTable = "create table metab("
            + "t_id integer primary key autoincrement,"
            + "t_tabname varchar(30) not null,"
            + "t_isMe integer not null)";

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTable);
        //在数据库不存在的时候创建一个数据库
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
