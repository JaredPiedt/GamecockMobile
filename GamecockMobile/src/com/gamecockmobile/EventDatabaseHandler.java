package com.gamecockmobile;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabaseHandler extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  private static final String TABLE_EVENTS = "events";

  private static final String KEY_ID = "id";
  private static final String KEY_NAME = "title";
  private static final String KEY_DATE = "date";
  private static final String KEY_TYPE = "type";
  private static final String KEY_NOTIFICATIONS = "notifications";

  public EventDatabaseHandler(Context context, String name) {
    super(context, name, null, DATABASE_VERSION);
    // TODO Auto-generated constructor stub

  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub
    String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" + KEY_ID
        + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_DATE + " TEXT" + KEY_TYPE + " TEXT"
        + KEY_NOTIFICATIONS + " TEXT" + ")";
    db.execSQL(CREATE_COURSES_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

    onCreate(db);
  }
  
  public void addEvent(Event event){
    SQLiteDatabase db = this.getWritableDatabase();
    
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, event.getName());
    values.put(KEY_DATE, Long.toString(event.getDate()));
    values.put(KEY_TYPE, event.getType());
    values.put(KEY_NOTIFICATIONS, event.getNotifications().toString());
    
    db.insert(TABLE_EVENTS, null, values);
    db.close();
  }
  
  public Event getEvent(int id){
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_EVENTS, new String[] { KEY_ID, KEY_NAME, KEY_DATE, KEY_TYPE, KEY_NOTIFICATIONS },
        KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
      cursor.moveToFirst();

    Event event = new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
        cursor.getString(2), cursor.getString(3), cursor.getString(4));
    // return course
    return event;
  }
  
  public ArrayList<Event> getAllEvents() {
    ArrayList<Event> eventList = new ArrayList<Event>();
    String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        Event event = new Event();
        event.setId(Integer.parseInt(cursor.getString(0)));
        event.setName(cursor.getString(1));
        event.setDateFromString(cursor.getString(2));
        event.setType(cursor.getString(3));
        event.setNotificationsFromString(cursor.getString(4));

        eventList.add(event);
      } while (cursor.moveToNext());
    }

    return eventList;
  }
  
  public int getCoursesCount() {
    String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int count = cursor.getCount();
    cursor.close();

    // return count
    return count;
  }
  
  public int updateCourse(Event event, Context context) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, event.getName());
    values.put(KEY_DATE, Long.toString(event.getDate()));
    values.put(KEY_TYPE, event.getType());
    values.put(KEY_NOTIFICATIONS, event.getNotifications().toString());

    // updating row
    return db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
        new String[] { String.valueOf(event.getId()) });
  }
  
  public void deleteEvent(Event event) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_EVENTS, KEY_ID + " = ?", new String[] { String.valueOf(event.getId()) });
    db.close();
  }


}
