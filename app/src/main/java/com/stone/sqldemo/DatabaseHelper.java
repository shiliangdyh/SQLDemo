package com.stone.sqldemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.github.commonlib.utils.LogUtils;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 28;
    public static final String DATABASE_NAME = "test.db";
    private static DatabaseHelper databaseHelper;

    public static final String TABLE_TEST = "Test";

    public static final String SQL_CREATE_TEST = "create table Test ( _id INTEGER PRIMARY KEY AUTOINCREMENT,name text not null,job text ,age INTEGER )";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (null == databaseHelper) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Test 表1.0
        db.execSQL(SQL_CREATE_TEST);
//        onUpgrade(db, 1, DATABASE_VERSION);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.d(TAG, "onUpgrade: oldVersion=" + oldVersion + " ,newVersion=" + newVersion);

        try {
            db.beginTransaction();
            // rename the table
            String tempTable = TABLE_TEST + "texp_temptable";
            String renameTableSql = "alter table " + TABLE_TEST + " rename to " + tempTable;
            db.execSQL(renameTableSql);// drop the oldtable
            String dropTableSql = "drop table if exists " + TABLE_TEST;
            db.execSQL(dropTableSql);
            // creat table
            String createTableSql = "create table if not exists Test ( _id INTEGER PRIMARY KEY AUTOINCREMENT,name text not null,pwd text ,age INTEGER )";
            db.execSQL(createTableSql);
            // load data
            String insertSql = "INSERT INTO Test (_id,name,age) SELECT _id,name,age FROM texp_temptable";
            db.execSQL(insertSql);
            //Drop temp table
            String deleteSql = "DROP TABLE IF EXISTS "+tempTable;
            db.execSQL(deleteSql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            LogUtils.d(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public interface UserColumns extends BaseColumns {
        String NAME = "name";
        String JOB = "job";
        String AGE = "age";
    }
}
