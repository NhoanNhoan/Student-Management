package com.example.schoolmanagement.database_handler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class StudentPositionHandler {
    private StudentManagementDatabaseHandler handler;

    public StudentPositionHandler(Context context) {
        handler = new StudentManagementDatabaseHandler(context);
    }

    public ArrayList<String> getAllStudentPosition() {
        SQLiteDatabase sqLiteDatabase = handler.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(StudentManagementDatabaseHandler.STUDENT_POSITION_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (null != cursor) {
            cursor.moveToFirst();
        }

        ArrayList<String> listPosition = new ArrayList<>();
        while (null != cursor && !cursor.isAfterLast()) {
            listPosition.add(cursor.getString(1));
            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return listPosition;
    }

    public String getPositionById(int id) {
        SQLiteDatabase sqLiteDatabase = handler.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(StudentManagementDatabaseHandler.STUDENT_POSITION_TABLE_NAME,
                null,
                "id = ?",
                new String[] {Integer.toString(id)},
                null, null, null);

        if (null != cursor) {
            cursor.moveToFirst();
        }

        if (0 != cursor.getCount()) {
            return cursor.getString(1);
        }

        return null;
    }

    public Integer findId(String studentPosition) {
        SQLiteDatabase sqLiteDatabase = handler.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase.query(StudentManagementDatabaseHandler.STUDENT_POSITION_TABLE_NAME,
                new String[] {StudentManagementDatabaseHandler.KEY_STUDENT_POSITION_ID},
                StudentManagementDatabaseHandler.KEY_STUDENT_POSITION_TYPE + " = ?",
                new String[] {studentPosition},
                null, null, null);

        if (null != cursor) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() == 0) {
            return null;
        }

        return cursor.getInt(0);
    }

    public void insertStudentPosition(String positionName) {
        SQLiteDatabase sqLiteDatabase = handler.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_POSITION_TYPE, positionName);

        sqLiteDatabase.insert(StudentManagementDatabaseHandler.STUDENT_POSITION_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }
}
