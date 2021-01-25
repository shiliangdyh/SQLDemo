package com.stone.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static void insert(Context context, User user) {
        ContentValues values = new ContentValues();
        bindAppValue(values, user);
        context.getContentResolver().insert(MyProvider.TEST_URI, values);
    }

    public static List<User> queryUsers(Context context) {
        List<User> users = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MyProvider.TEST_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                User user = User.readCursor(cursor);
                users.add(user);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return users;
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

    // update table
    private void updateTable(SQLiteDatabase db, String tableName, String columns) {
        try {
            db.beginTransaction();
            String oldColumns = columns.substring(0, columns.length() - 1);
            // rename the table
            String tempTable = tableName + "texp_temptable";
            String renameTableSql = "alter table " + tableName + " rename to " + tempTable;
            db.execSQL(renameTableSql);// drop the oldtable
            String dropTableSql = "drop table if exists " + tableName;
            db.execSQL(dropTableSql);
            // creat table
            String createTableSql = "create table if not exists " + tableName + "(name text, pwd text, tel text)";
            db.execSQL(createTableSql);
            // load data
            String newColumn = "tel";
            String newColumns = oldColumns + "," + newColumn;
            String insertSql = "insert into " + tableName + " (" + newColumns + ") " + "select " + oldColumns + "" + " " + " from " + tempTable;
            db.execSQL(insertSql);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("tag", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }


    private static void bindAppValue(ContentValues values, User user) {
        values.put(DatabaseHelper.UserColumns.NAME, user.getName());
        values.put(DatabaseHelper.UserColumns.JOB, user.getJob());
        values.put(DatabaseHelper.UserColumns.AGE, user.getAge());
    }
}
