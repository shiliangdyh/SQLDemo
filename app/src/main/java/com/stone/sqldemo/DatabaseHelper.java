package com.stone.sqldemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.github.commonlib.utils.LogUtils;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 29;
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

        LogUtils.d(TAG, "onUpgrade: 更新前表字段 " + getColumnNames(db, TABLE_TEST));
        try {
            db.beginTransaction();
            // rename the table
            String tempTable = "texp_temptable";
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
            String deleteSql = "DROP TABLE IF EXISTS " + tempTable;
            db.execSQL(deleteSql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            LogUtils.d(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
        LogUtils.d(TAG, "onUpgrade: 更新后表字段 " + getColumnNames(db, TABLE_TEST));
        LogUtils.d(TAG, "onUpgrade: success-->");
    }


    // 获取升级前表中的字段
    public static String getColumnNames(SQLiteDatabase db, String tableName) {
        StringBuffer columnNameBuffer = null;
        Cursor c = null;
        try {
            c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (null != c) {
                int columnIndex = c.getColumnIndex("name");
                if (-1 == columnIndex) {
                    return null;
                }
                int index = 0;
                columnNameBuffer = new StringBuffer(c.getCount());
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    columnNameBuffer.append(c.getString(columnIndex));
                    columnNameBuffer.append(",");
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return columnNameBuffer.toString();
    }

    public interface UserColumns extends BaseColumns {
        String NAME = "name";
        String JOB = "job";
        String AGE = "age";
        String PWD = "pwd";
    }
}
