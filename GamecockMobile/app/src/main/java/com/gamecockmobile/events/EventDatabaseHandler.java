package com.gamecockmobile.events;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class handles all of the methods for creating and upgrading the database containing all of
 * the events and also all of the CRUD operations
 */
public class EventDatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Events.db";
    private static final String TABLE_EVENTS = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_COURSE = "course";
    private static final String KEY_NAME = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NOTIFICATIONS = "notifications";

    public EventDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

    }

    /**
     * Method to create the database.
     *
     * @param db the SQLiteDatabase you want to create
     */
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_COURSE + " TEXT," + KEY_TYPE + " TEXT," + KEY_DATE
                + " TEXT," + KEY_START_TIME + " TEXT," + KEY_END_TIME + " TEXT," + KEY_NOTIFICATIONS + " TEXT" + ")";
        db.execSQL(CREATE_COURSES_TABLE);
    }

    /**
     * Method to upgrade the database
     *
     * @param db         the SQLiteDatabase to upgrade
     * @param oldVersion the 'int' value of the oldVersion
     * @param newVersion the 'int' value of the newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        onCreate(db);
    }

    /**
     * Method to add an event.
     *
     * @param event the 'Event' to add
     */
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_COURSE, event.getCourse());
        values.put(KEY_TYPE, event.getType());
        values.put(KEY_DATE, Long.toString(event.getDate()));
        values.put(KEY_START_TIME, Long.toString(event.getStartTime()));
        values.put(KEY_END_TIME, Long.toString(event.getEndTime()));
        values.put(KEY_NOTIFICATIONS, "3");
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    /**
     * Method to get an 'Event'.
     *
     * @param id the 'int' value of the id for the 'Event' to get
     * @return the 'Event' at the specified id
     */
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, new String[]{KEY_ID, KEY_NAME, KEY_COURSE, KEY_TYPE,
                        KEY_DATE, KEY_START_TIME, KEY_END_TIME, KEY_NOTIFICATIONS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null,
                null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        System.out.println(cursor.getString(5));

        return event;
    }

    public Event getEvent(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, new String[]{KEY_ID, KEY_NAME, KEY_COURSE, KEY_TYPE,
                KEY_DATE, KEY_START_TIME, KEY_END_TIME, KEY_NOTIFICATIONS}, KEY_NAME + "=?", new String[]{name}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

        return event;
    }

    /**
     * Method to get all 'Events'.
     *
     * @return an 'ArrayList' of all the 'Events'
     */
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " ORDER BY " + KEY_DATE + ", " + KEY_START_TIME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setCourse(cursor.getString(2));
                event.setType(Integer.valueOf(cursor.getString(3)));
                event.setDateFromString(cursor.getString(4));
                event.setStartTimeFromString(cursor.getString(5));
                event.setEndTimeFromString(cursor.getString(6));
                event.setNotificationsFromString(cursor.getString(7));

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        return eventList;
    }

    /**
     * Method to get the number of events in the database.
     *
     * @return the number of events
     */
    public int getEventsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Method to upgrade an 'Event'.
     *
     * @param event   the 'Event' you want to get
     * @param context the context of the application
     * @return the id of the updated 'Event'
     */
    public int updateCourse(Event event, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_COURSE, event.getCourse());
        values.put(KEY_TYPE, event.getType());
        values.put(KEY_DATE, Long.toString(event.getDate()));
        values.put(KEY_START_TIME, Long.toString(event.getStartTime()));
        values.put(KEY_END_TIME, Long.toString(event.getEndTime()));
        values.put(KEY_NOTIFICATIONS, event.getNotifications().toString());

        // updating row
        return db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
    }

    /**
     * Method to delete an 'Event'
     *
     * @param event the 'Event' you want to delete
     */

    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_ID + " = ?", new String[]{String.valueOf(event.getId())});
        db.close();
    }

}
