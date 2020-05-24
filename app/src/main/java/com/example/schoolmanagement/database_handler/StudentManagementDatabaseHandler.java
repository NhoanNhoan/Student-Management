package com.example.schoolmanagement.database_handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class StudentManagementDatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "STUDENT MANAGEMENT";
    private static final int DB_VERSION = 1;

    static final String STUDENT_TABLE_NAME = "STUDENT";
    static final String KEY_STUDENT_ID = "id";
    static final String KEY_STUDENT_NAME = "name";
    static final String KEY_STUDENT_STUDENT_POSITION = "position_id";
    static final String KEY_STUDENT_PHONE = "phone_number";
    static final String KEY_STUDENT_SEX = "sex";
    static final String KEY_STUDENT_EMAIL = "email";
    static final String KEY_STUDENT_BIRTH_DATE = "birth_date";
    static final String KEY_STUDENT_AVERAGE_SCORE = "avg_score";
    static final String KEY_STUDENT_AVATAR = "avatar";
    static final String KEY_STUDENT_STATE = "state";

    static final String STUDENT_POSITION_TABLE_NAME = "STUDENT_POSITION";
    static final String KEY_STUDENT_POSITION_ID = "id";
    static final String KEY_STUDENT_POSITION_TYPE = "type";


    StudentManagementDatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createStudentPositionTable(db);
        createStudentTable(db);
    }

    private void createStudentTable(SQLiteDatabase db) {
        String createStudentTableStatement = String.format("CREATE TABLE %s(%s STRING PRIMARY KEY, " +
                        "%s STRING, " +
                        "%s STRING, " +
                        "%s STRING, " +
                        "%s INTEGER," +
                        "%s DATE, " +
                        "%s REAL, " +
                        "%s BLOB, " +
                        "%s STRING, " +
                        "%s STRING, " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s))",
                STUDENT_TABLE_NAME,
                KEY_STUDENT_ID,
                KEY_STUDENT_NAME,
                KEY_STUDENT_PHONE,
                KEY_STUDENT_EMAIL,
                KEY_STUDENT_STUDENT_POSITION,
                KEY_STUDENT_BIRTH_DATE,
                KEY_STUDENT_AVERAGE_SCORE,
                KEY_STUDENT_AVATAR,
                KEY_STUDENT_STATE,
                KEY_STUDENT_SEX,
                KEY_STUDENT_STUDENT_POSITION,
                STUDENT_POSITION_TABLE_NAME,
                KEY_STUDENT_POSITION_ID);

        db.execSQL(createStudentTableStatement);
    }

    private void createStudentPositionTable(SQLiteDatabase db) {
        String createStudentPositionTableStatement = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
                STUDENT_POSITION_TABLE_NAME,
                KEY_STUDENT_POSITION_ID,
                KEY_STUDENT_POSITION_TYPE);

        db.execSQL(createStudentPositionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropStudentTableStatement = String.format("DROP TABLE IF EXISTS %s", STUDENT_TABLE_NAME);
        db.execSQL(dropStudentTableStatement);
        onCreate(db);
    }
}
