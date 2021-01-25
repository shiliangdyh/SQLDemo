package com.stone.sqldemo;

import android.database.Cursor;

public class User {
    private String name;
    private String job;
    private int age;

    public static
    User readCursor(Cursor cursor) {

        User user = new
                User();
        user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UserColumns.NAME)));
        user.setJob(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UserColumns.JOB)));
        user.setAge(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.UserColumns.AGE)));
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
