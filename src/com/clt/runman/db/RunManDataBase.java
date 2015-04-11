package com.clt.runman.db;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.clt.runman.R;
import com.clt.runman.utils.StringUtils;

/**
 * 操作数据库的实体类
 * @author yanshengli
 *
 */
public class RunManDataBase {

    /**
     * 实体类对象
     */
    private static RunManDataBase instance;

    /**
     * 数据文件名
     */
    private static final String   defaultDBName = "RunMan.sql";

    /**
     * 数据库对象
     */
    private FinalDb               finalDB;

    /**
     * 私有的构造方法
     */
    private RunManDataBase(Context context) {
        // 1.数据库文件名
        String dbName = context.getResources ().getString (R.string.FinalDB_DBName);
        if (StringUtils.isEmpty (dbName)) {
            dbName = defaultDBName;
        }

        // 2.是否是debug模式
        String debug = context.getResources ().getString (R.string.FinalDB_IsDebug);
        boolean isDebug = false;
        if (!StringUtils.isEmpty (debug)) {
            isDebug = Boolean.parseBoolean (debug);
        }
        
        // 4.数据库版本
        String version = StringUtils.trimNull (context.getResources ().getString (R.string.FinalDB_Version), "1");
   
        // 3.创建对象
        finalDB = FinalDb.create (context, dbName, isDebug,Integer.parseInt (version),new DbUpdateListener() {
            @Override
            public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
                if(oldVersion < newVersion)
                {
                    dropDb(db);
                }
            }
        });
    }
    
    /**
     * 删除所有数据表
     */
    public void dropDb(SQLiteDatabase db){
        Cursor cursor = db.rawQuery ("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext ()) {
                db.execSQL ("DROP TABLE " + cursor.getString (0));
            }
        }
        if (cursor != null) {
            cursor.close ();
            cursor = null;
        }
    }

    /**
     * 获取数据库对象
     * @return
     */
    public FinalDb database(){
        return finalDB;
    }

    /**
     * 获取数据库对象
     * @return
     */
    public static RunManDataBase newInstance(Context context){
        if (instance == null) {
            instance = new RunManDataBase (context);
        }

        return instance;
    }
}
