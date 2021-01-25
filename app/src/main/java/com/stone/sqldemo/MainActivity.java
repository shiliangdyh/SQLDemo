package com.stone.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.commonlib.utils.LogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
        String columnNames = DatabaseHelper.getColumnNames(db, DatabaseHelper.TABLE_TEST);
        LogUtils.d(TAG, "onCreate: " + columnNames);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setAge(10);
                user.setPwd("100110");
                user.setName("shilan");
                UserDAO.insert(MainActivity.this, user);
            }
        });
        findViewById(R.id.log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> users = UserDAO.queryUsers(MainActivity.this);
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    LogUtils.d(TAG, "onClick: " + user);
                }
            }
        });
    }
}