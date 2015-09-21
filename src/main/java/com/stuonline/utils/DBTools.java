package com.stuonline.utils;

import java.util.ArrayList;
import java.util.List;
import com.stuonline.entity.MeTab;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBTools  {

	private DBHelper helper;
	private static String name="constans";
	private static int banbei=1;
	private SQLiteDatabase db;
	private static final String table="metab";
	public DBTools(Context context) {
		helper=new DBHelper(context, name, banbei);
	}
public List<MeTab> queryAll(){
	List<MeTab> meTabs=new ArrayList<MeTab>();
	db=helper.getReadableDatabase();
	Cursor cursor=db.query(table,new String[]{"t_id","t_tabname"}, null, null, null, null, null);
	while (cursor!=null&& cursor.moveToNext()) {
		MeTab meTab=tabcreat(cursor);
		meTabs.add(meTab);
	}
	cursor.close();
	db.close();
	return meTabs;
}

private MeTab tabcreat(Cursor cursor){
	MeTab meTab=new MeTab();
	   int id=cursor.getInt(cursor.getColumnIndex("t_id"));
		String name=cursor.getString(cursor.getColumnIndex("t_tabname"));

	meTab.setT_id(id);
	meTab.setT_tabname(name);

		return meTab;
}
//	private boolean updateeMeTab(){
//		db=helper.getReadableDatabase();
//		db.update(table,new String[]{"t_id","t_tabname"}, null, null, null, null,null);
//		return meTab;
//	}
}
