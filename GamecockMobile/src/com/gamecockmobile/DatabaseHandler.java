package com.gamecockmobile;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class handles all of the methods for creating and upgrading the database containing all of
 * the courses and also all of the CRUD operations
 */
public class DatabaseHandler extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  static final String DATABASE_NAME = "CoursesManager.db";
  static final String TABLE_COURSES = "courses";
  Context context;

  private static final String KEY_ID = "id";
  static final String KEY_NAME = "name";
  private static final String KEY_CLASS_TIMES = "class_times";
  private static final String KEY_EVENTS_DB = "events_db";

  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  /**
   * Method to create the database
   * 
   * @param db
   *          the SQLiteDatabase you want to create
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "(" + KEY_ID
        + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_CLASS_TIMES + " TEXT," + KEY_EVENTS_DB + " TEXT" + ")";
    db.execSQL(CREATE_COURSES_TABLE);
  }

  /**
   * Method to upgrade the database
   * 
   * @param db
   *          the SQLiteDatabase to upgrade
   * @param oldVersion
   *          the 'int' value of the oldVersion
   * @param newVersion
   *          the 'int' value of the newVersion
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);

    onCreate(db);
  }

  /**
   * Method to add a course.
   * 
   * @param course
   *          the 'Course' to add
   * @param context
   *          the context of the application
   */
  public void addCourse(Course course, Context context) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, course.getCourseName());
    values.put(KEY_CLASS_TIMES, course.classTimesToString(context));
    values.put(KEY_EVENTS_DB, course.getCourseName().replaceAll("\\s+", "") + ".db");
    
    Log.d("Inserting", course.classTimesToString(context));

    db.insert(TABLE_COURSES, null, values);
    db.close();
  }

  /**
   * Method to get a 'Course'.
   * 
   * @param id
   *          the 'int' value of the id for the 'Course' to get
   * @return the 'Course' at the specified id
   */
  public Course getCourse(int id) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_COURSES, new String[] { KEY_ID, KEY_NAME, KEY_CLASS_TIMES },
        KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
      cursor.moveToFirst();

    Course course = new Course(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
        cursor.getString(2));
    // return course
    return course;
  }

  public Course getCourseByName(String name) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_COURSES, new String[] { KEY_ID, KEY_NAME, KEY_CLASS_TIMES },
        KEY_NAME + "=?", new String[] { name }, null, null, null, null);
    if(cursor != null)
      cursor.moveToFirst();
    
    Course course = new Course(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
        cursor.getString(2));
    
    return course;
  }

  /**
   * Method to get all 'Courses'.
   * 
   * @return and 'ArrayList' of all the 'Courses'
   */
  public ArrayList<Course> getAllCourses() {
    ArrayList<Course> courseList = new ArrayList<Course>();
    String selectQuery = "SELECT  * FROM " + TABLE_COURSES;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        Course course = new Course();
        course.setID(Integer.parseInt(cursor.getString(0)));
        course.setCourseName(cursor.getString(1));
        course.setClassTimesFromString(cursor.getString(2));

        courseList.add(course);
      } while (cursor.moveToNext());
    }

    return courseList;
  }

  /**
   * Method to get the number of course in the database.
   * 
   * @return the number of courses
   */
  public int getCoursesCount() {
    String countQuery = "SELECT  * FROM " + TABLE_COURSES;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int count = cursor.getCount();
    cursor.close();

    // return count
    return count;
  }

  /**
   * Method to upgrade a 'Course'.
   * 
   * @param course
   *          the 'Course' you want to get
   * @param context
   *          the context of the application
   * @return the id of the updated 'Course'
   */
  public int updateCourse(Course course, Context context) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, course.getCourseName());
    values.put(KEY_CLASS_TIMES, course.classTimesToString(context));

    // updating row
    return db.update(TABLE_COURSES, values, KEY_ID + " = ?",
        new String[] { String.valueOf(course.getID()) });
  }

  /**
   * Method to delete a 'Course'
   * 
   * @param course
   *          the 'Course' you want to delete
   */
  public void deleteCourse(Course course) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_COURSES, KEY_ID + " = ?", new String[] { String.valueOf(course.getID()) });
    db.close();
  }
  
  

}
