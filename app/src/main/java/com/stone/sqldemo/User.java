package com.stone.sqldemo;

import android.database.Cursor;

public class User {
    private String name;
    private String pwd;
    private int age;

    public static
    User readCursor(Cursor cursor) {

        User user = new
                User();
        user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UserColumns.NAME)));
        user.setAge(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.UserColumns.AGE)));
        user.setPwd(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UserColumns.PWD)));
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
