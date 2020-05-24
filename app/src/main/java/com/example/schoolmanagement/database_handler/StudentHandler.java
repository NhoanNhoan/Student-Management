package com.example.schoolmanagement.database_handler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.schoolmanagement.data_class.Student;
import com.example.schoolmanagement.enums.Sex;
import com.example.schoolmanagement.utils.BitmapUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentHandler {
    private StudentManagementDatabaseHandler dbHandler;
    private StudentPositionHandler studentPositionHandler;

    public StudentHandler(Context context) {
        dbHandler = new StudentManagementDatabaseHandler(context);
        studentPositionHandler = new StudentPositionHandler(context);
    }

    public ArrayList<Student> getAllStudents() throws ParseException {
        SQLiteDatabase sqLiteDatabase = null;

        if (null != dbHandler) {
             sqLiteDatabase = dbHandler.getReadableDatabase();
        }
        else {
            return null;
        }

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME,
                null, StudentManagementDatabaseHandler.KEY_STUDENT_STATE + " = ?", new String[] {"available"}, null, null, null);

        if (null != cursor) {
            cursor.moveToFirst();
        }

        ArrayList<Student> listStudent = new ArrayList<>();

        if (cursor.getCount() == 0) {
            return listStudent;
        }

        while (null != cursor && !cursor.isAfterLast()) {
            Integer studentPositionId = cursor.getInt(4);
            String studentPosition = null;
            if (null != studentPositionId) {
                studentPosition = studentPositionHandler.getPositionById(cursor.getInt(4));
            }
            Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(5));
            Bitmap profile = BitmapUtility.getBitmap(cursor.getBlob(7));

            @SuppressLint("SimpleDateFormat") Student student = new Student(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    studentPosition,
                    birthDate,
                    profile);
            student.setSex(cursor.getString(9).equals("male") ? Sex.MALE : Sex.FEMALE);

            listStudent.add(student);
            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return listStudent;
    }

    @SuppressLint("SimpleDateFormat")
    public void insertStudent(Student student) {
        SQLiteDatabase sqLiteDatabase = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Integer studentPositionId = null;

        if (null != student.getPosition()) {
            studentPositionId = studentPositionHandler.findId(student.getPosition());
        }

        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_ID, student.getId());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_NAME, student.getName());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_PHONE, student.getPhone());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_EMAIL, student.getEmail());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_SEX, student.getSex() == Sex.MALE ? "male" : "female");
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_STUDENT_POSITION, studentPositionId);
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_BIRTH_DATE, new SimpleDateFormat("dd/MM/yyyy").format(student.getBirthDate()));
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_AVATAR, BitmapUtility.getBytes(student.getAvatar()));
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_STATE, "available");

        sqLiteDatabase.insert(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME,null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<Integer> findId(Student student) {
        ArrayList<Integer> listId = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        SimpleDateFormat birthDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Cursor cursor = sqLiteDatabase.query(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME, null,
                StudentManagementDatabaseHandler.KEY_STUDENT_NAME + " = ? AND "
                        + StudentManagementDatabaseHandler.KEY_STUDENT_POSITION_TYPE + " = ? AND "
                        + StudentManagementDatabaseHandler.KEY_STUDENT_PHONE + " = ? AND "
                        + StudentManagementDatabaseHandler.KEY_STUDENT_EMAIL + " = ? AND "
                        + StudentManagementDatabaseHandler.KEY_STUDENT_BIRTH_DATE + " = ?",
                new String[] {student.getName(),
                        student.getPosition(),
                        student.getPhone(),
                        student.getEmail(),
                        birthDateFormat.format(student.getBirthDate())}, null, null, null);

        if (null != cursor) {
            cursor.moveToFirst();
        }

        if (0 == cursor.getCount()) {
            return listId;
        }

        while (!cursor.isAfterLast()) {
            listId.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        return listId;
    }

    public void deleteStudent(String id) {
        SQLiteDatabase sqLiteDatabase = dbHandler.getWritableDatabase();
        sqLiteDatabase.delete(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME,
                StudentManagementDatabaseHandler.KEY_STUDENT_ID + " = ?",
                new String[] {id});

        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_STATE, "removed");

        sqLiteDatabase.update(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME,
                contentValues,
                StudentManagementDatabaseHandler.KEY_STUDENT_STATE + " = ?",
                new String[] {id});
        sqLiteDatabase.close();
    }

    public boolean wasExistId(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME, null, StudentManagementDatabaseHandler.KEY_STUDENT_ID + " = ?",
                new String[] {id}, null, null, null);
        return (null != cursor) && 0 != cursor.getCount();
    }

    @SuppressLint("SimpleDateFormat")
    public void update(Student student) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Integer studentPositionId = null;

        if (null != student.getPosition()) {
            studentPositionId = studentPositionHandler.findId(student.getPosition());
        }

        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_NAME, student.getName());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_PHONE, student.getPhone());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_EMAIL, student.getEmail());
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_STUDENT_POSITION, studentPositionId);
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_SEX, student.getSex() == Sex.MALE ? "male" : "female");
        if (null != student.getBirthDate())
            contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_BIRTH_DATE, new SimpleDateFormat("dd/MM/yyyy").format(student.getBirthDate()));
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_AVATAR, BitmapUtility.getBytes(student.getAvatar()));
        contentValues.put(StudentManagementDatabaseHandler.KEY_STUDENT_STATE, "available");

        db.update(StudentManagementDatabaseHandler.STUDENT_TABLE_NAME,
                contentValues,
                StudentManagementDatabaseHandler.KEY_STUDENT_ID + " = ?",
                new String[] {student.getId()});
        db.close();
    }
}
