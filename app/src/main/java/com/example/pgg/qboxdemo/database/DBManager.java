package com.example.pgg.qboxdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pgg.qboxdemo.greendao.db.DaoMaster;

/**
 * Created by pgg on 2018/5/5.
 *
 * 数据库管理类
 */

public class DBManager {

    private final static String dbName="lastbox_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private DBManager(Context context){
        this.context=context;
        openHelper=new DaoMaster.DevOpenHelper(context,dbName,null);
    }

    public static DBManager getInstance(Context context){
        if (mInstance==null){
            synchronized (DBManager.class){
                if (mInstance==null){
                    mInstance=new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     * @return
     */
    protected SQLiteDatabase getReadableDatabase(){
        if (openHelper==null){
            openHelper=new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     * @return
     */
    protected SQLiteDatabase getWriteableDatabase(){
        if (openHelper==null){
            openHelper=new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        return writableDatabase;
    }
}
