
package com.stone.sqldemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyProvider extends ContentProvider {

    private static UriMatcher MATCHER;
    public static String PROVIDER = "%s.provider";
    public static Uri TEST_URI;

    private static final int TABLE_TEST_CODE = 1;
    private DatabaseHelper databaseHelper;


    private void init() {
        PROVIDER = String.format(PROVIDER, getContext().getPackageName() + ".db");

        TEST_URI = Uri.parse("content://" + PROVIDER + "/" + DatabaseHelper.TABLE_TEST);


        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(PROVIDER, DatabaseHelper.TABLE_TEST, TABLE_TEST_CODE);
    }

    @Override
    public boolean onCreate() {
        init();
        databaseHelper = DatabaseHelper.getInstance(getContext());
        try {
            databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            databaseHelper.close();
            databaseHelper = null;
            e.printStackTrace();
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        String tableName = getOptTableName(uri);
        sqLiteQueryBuilder.setTables(tableName);
        SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
        return sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case TABLE_TEST_CODE:
                return DatabaseHelper.TABLE_TEST;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getOptTableName(uri);
        long result = -1;
        SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            result = db.replace(tableName, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (result != -1) {
            return uri;
        } else {
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = -1;
        String tableName = getOptTableName(uri);
        SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            result = db.delete(tableName, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = -1;
        SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
        String tableName = getOptTableName(uri);
        db.beginTransaction();
        try {
            result = db.update(tableName, values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (SQLiteFullException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    private String getOptTableName(Uri uri) {
        int table = MATCHER.match(uri);
        switch (table) {
            case TABLE_TEST_CODE:
                return DatabaseHelper.TABLE_TEST;
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    private SQLiteOpenHelper getDatabaseHelper() {
        return databaseHelper;
    }

    /**
     * 这个可以加条件
     * @param selection
     * @param tableName
     * @return
     */
//    private String getNeedPhoneNoSelection(String selection, String tableName) {
//        if (judgeTableNameNeedPhoneNo(tableName)){
//            String phoneNo = MocamSetting.getInstance().getMobileNo();;
//            if (!TextUtils.isEmpty(phoneNo)) {
//                if (!TextUtils.isEmpty(selection)) {
//                    selection += " AND ";
//                }else {
//                    selection = "";
//                }
//                selection += DatabaseHelper.BaseColumns._USER_PHONENO + " = " + phoneNo;
//            }
//        }
//        return selection;
//    }
}
