package com.example.lucasnascimento.android_crud_sqlite.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lucasnascimento.android_crud_sqlite.objects.Course;

import java.util.ArrayList;

/**
 * Created by lucasnascimento on 15/10/17.
 */

public class BDSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CourseDB";
    private static final String TABLE = "course";
    SQLiteDatabase db;
    public BDSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE course ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name TEXT,"+
                "description TEXT,"+
                "classHours INTEGER,"+
                "registerDate DATETIME,"+
                "status BIT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS course");
        this.onCreate(sqLiteDatabase);
    }

    public void addCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put("name", course.getName());
        values.put("description", course.getDescription());
        values.put("classHours", new Integer(course.getClassHours()));
        values.put("registerDate", course.getRegisterDate());
        values.put("status", new Byte((byte)(course.getStatus()==true ? 1 : 0)));
        db.insert(TABLE, null, values);
        db.close();
    }

    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE + " WHERE id = "+ id;
        Cursor cursor = db.rawQuery(query, null);
        Course course = null;
        if (cursor.moveToFirst()) {
            course = parceCourse(cursor);
        }
        cursor.close();
        return course;
    }

    public ArrayList<Course> getCourses(String orderBy) {
        ArrayList<Course> list = new ArrayList<Course>();
        String query = "SELECT * FROM " + TABLE + " ORDER BY registerDate "+orderBy;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = parceCourse(cursor);
                list.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<Course> searchCourses(String orderBy,String like) {
        ArrayList<Course> list = new ArrayList<Course>();
        String query = "SELECT * FROM " + TABLE + " WHERE name LIKE '%"+like+"%' ORDER BY registerDate "+orderBy;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = parceCourse(cursor);
                list.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void removeCourse(Course course){
        db.delete(TABLE, "id = " + course.getId(), null);
        db.close();
    }

    public void updateCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put("name", course.getName());
        values.put("description", course.getDescription());
        values.put("classHours", new Integer(course.getClassHours()));
        values.put("status", new Byte((byte)(course.getStatus()==true ? 1 : 0)));
        db.update(TABLE, values, "id = " + course.getId(), null);
        db.close();
    }

    private Course parceCourse(Cursor cursor) {
        Course course = new Course();
        course.setId(Integer.parseInt(cursor.getString(0)));
        course.setName(cursor.getString(1));
        course.setDescription(cursor.getString(2));
        course.setClassHours(Integer.parseInt(cursor.getString(3)));
        course.setRegisterDate(cursor.getString(4));
        course.setStatus(Integer.parseInt(cursor.getString(5))==1);
        return course;
    }
}

