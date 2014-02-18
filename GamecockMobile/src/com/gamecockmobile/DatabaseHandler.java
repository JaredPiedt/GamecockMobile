package com.gamecockmobile;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "CoursesManager.db";
  private static final String TABLE_COURSES = "courses";
  
  private static final String KEY_ID = "id";
  private static final String KEY_NAME = "name";
  private static final String KEY_CLASS_TIMES = "class_times";
  
  public DatabaseHandler(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Creating Tables
  @Override
  public void onCreate(SQLiteDatabase db) {
      String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
              + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
              + KEY_CLASS_TIMES + " TEXT" + ")";
      db.execSQL(CREATE_COURSES_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
    
    onCreate(db);    
  }
  
  public void addCourse(Course course, Context context) {
    SQLiteDatabase db = this.getWritableDatabase();
    
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, course.getCourseName());
    values.put(KEY_CLASS_TIMES, course.classTimesToString(context));
    
    db.insert(TABLE_COURSES, null, values);
    db.close();
  }
  
  public Course getCourse(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
 
    Cursor cursor = db.query(TABLE_COURSES, new String[] { KEY_ID,
            KEY_NAME, KEY_CLASS_TIMES }, KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
        cursor.moveToFirst();
 
    Course course = new Course(Integer.parseInt(cursor.getString(0)),
            cursor.getString(1), cursor.getString(2));
    // return contact
    return course;
}
  
  public ArrayList<Course> getAllCourses() {
    ArrayList<Course> courseList = new ArrayList<Course>();
    String selectQuery = "SELECT  * FROM " + TABLE_COURSES;
    
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    
    if(cursor.moveToFirst()) {
      do {
        Course course = new Course();
        course.setID(Integer.parseInt(cursor.getString(0)));
        course.setCourseName(cursor.getString(1));
        course.setClassTimesFromString(cursor.getString(2));
        
        courseList.add(course);
      } while(cursor.moveToNext());
    }
    
    return courseList;
  }
  
  public int getCoursesCount() {
    String countQuery = "SELECT  * FROM " + TABLE_COURSES;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int count = cursor.getCount();
    cursor.close();

    // return count
    return count;
  }
  
  public int updateCourse(Course course, Context context) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, course.getCourseName());
    values.put(KEY_CLASS_TIMES, course.classTimesToString(context));
 
    // updating row
    return db.update(TABLE_COURSES, values, KEY_ID + " = ?",
            new String[] { String.valueOf(course.getID()) });
}
  
  public void deleteCourse(Course contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_COURSES, KEY_ID + " = ?",
            new String[] { String.valueOf(contact.getID()) });
    db.close();
}
  
  
}
