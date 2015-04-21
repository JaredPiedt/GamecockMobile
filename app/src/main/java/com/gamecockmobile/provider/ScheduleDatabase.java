package com.gamecockmobile.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;

import com.gamecockmobile.Course;
import com.gamecockmobile.R;
import com.gamecockmobile.events.Event;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static com.gamecockmobile.util.LogUtils.*;
/**
 * Created by piedt on 12/30/14.
 */
public class ScheduleDatabase extends SQLiteOpenHelper {

    private static final String TAG = makeLogTag(ScheduleDatabase.class);

    private static final String DATABASE_NAME = "schedule.db";

    private static final int CUR_DATABASE_VERSION = 1;

    private final Context mContext;

    interface Tables {
        String DEPARTMENTS = "departments";
        String COURSES = "courses";
        String SECTIONS = "sections";
        String INSTRUCTORS = "instructors";
        String LOCATIONS = "locations";
        String TIME_PERIODS = "time_periods";
        String MY_COURSES = "my_courses";
        String EVENTS = "events";
        String EVENT_TYPES = "event_types";
    }

    interface DepartmentsColumns {
        /** Unique string identifying this department **/
        String DEPARTMENT_ID = "department_id";
        /** Name describing this department **/
        String DEPARTMENT_NAME = "department_name";
    }

    interface CoursesColumns {
        /** Unique string identifying this course **/
        String COURSE_ID = "course_id";
        /** **/
        String DEPARTMENT_ID = "department_id";
        /** Name describing this course **/
        String COURSE_NAME = "course_name";
        /** Number describing this course **/
        String COURSE_NUMBER = "course_number";
    }

    interface SectionsColumns {
        /** Unique string identifying this section **/
        String SECTION_ID = "section_id";
        /** String identifying the section number **/
        String SECTION_NUM = "section_num";
        /** Foreign key referencing the course associated to this section **/
        String COURSE_ID = "course_id";
        /** Foreign key referencing the instructor associated to this section **/
        String INSTRUCTOR_ID = "instructor_id";
        /** Foreign key referencing the location associated to this section **/
        String LOCATION_ID = "location_id";
        /** Days this section occurs **/
        String TP_ID = "tp_id";
        /** Number of credits association to this section **/
        String CREDITS = "credits";
    }

    interface InstructorsColumns {
        /** Unique string identifying this instructor **/
        String INSTRUCTOR_ID = "instructor_id";
        /** Instructor name **/
        String INSTRUCTOR_NAME = "instructor_name";
    }

    interface LocationsColumns {
        /** Unique string identifying this time period **/
        String LOCATION_ID = "location_id";
        /** Location name **/
        String LOCATION_NAME = "location_name";
    }

    interface TimePeriods {
        /** Unique string identifying this time period **/
        String TP_ID = "tp_id";
        /** Description of time period **/
        String TP_DESCRIPTION = "tp_description";
    }

    interface MyCoursesColumns {
        /** Foreign key identifying the dept id **/
        String DEPARTMENT_ID = "dept_id";
        /** Foreign key identifying the course id **/
        String COURSE_ID = "course_id";
        /** Foreign key identifying the section id **/
        String SECTION_ID = "section_id";
    }

    interface EventsColumns {
        /** Foreign key identifying the my course id **/
        String COURSE_ID = "course_id";
        /** String identifying the event title **/
        String TITLE = "title";
        /** String identifying the date **/
        String DATE = "date";
        /** String identifying the start time **/
        String START_TIME = "start_time";
        /** String identifying the end time **/
        String END_TIME = "end_time";
        /** Foreign key identifying the type **/
        String TYPE_ID = "type_id";
        /** String identifying the notifications **/
        String NOTIFICATION = "notification";
    }

    interface EventTypesColumns {
        /** String identifying the type **/
        String TYPE = "type";
    }

    public ScheduleDatabase(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.DEPARTMENTS + " ("
                + DepartmentsColumns.DEPARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DepartmentsColumns.DEPARTMENT_NAME + " TEXT NOT NULL, "
                + "UNIQUE (" + DepartmentsColumns.DEPARTMENT_NAME + "))");


        db.execSQL("CREATE TABLE " + Tables.COURSES + " ("
                + CoursesColumns.COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CoursesColumns.DEPARTMENT_ID + " INTEGER NOT NULL,"
                + CoursesColumns.COURSE_NAME + " TEXT NOT NULL,"
                + CoursesColumns.COURSE_NUMBER + " TEXT NOT NULL, "
                + "UNIQUE (" + CoursesColumns.DEPARTMENT_ID + ", " + CoursesColumns.COURSE_NUMBER + "))");

        db.execSQL("CREATE TABLE " + Tables.SECTIONS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SectionsColumns.SECTION_NUM + " TEXT NOT NULL,"
                + SectionsColumns.SECTION_ID + " TEXT NOT NULL,"
                + SectionsColumns.COURSE_ID + " INTEGER NOT NULL,"
                + SectionsColumns.INSTRUCTOR_ID + " INTEGER NOT NULL,"
                + SectionsColumns.LOCATION_ID + " INTEGER NOT NULL,"
                + SectionsColumns.TP_ID + " INTEGER NOT NULL,"
                + SectionsColumns.CREDITS + " TEXT NOT NULL, " +
                "UNIQUE (" + SectionsColumns.SECTION_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.INSTRUCTORS + " ("
                + InstructorsColumns.INSTRUCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + InstructorsColumns.INSTRUCTOR_NAME + " TEXT NOT NULL, " +
                "UNIQUE (" + InstructorsColumns.INSTRUCTOR_NAME + "))");

        db.execSQL("CREATE TABLE " + Tables.LOCATIONS + " ("
                + LocationsColumns.LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LocationsColumns.LOCATION_NAME + " TEXT NOT NULL, " +
                "UNIQUE (" + LocationsColumns.LOCATION_NAME + "))");

        db.execSQL("CREATE TABLE " + Tables.TIME_PERIODS + " ("
                + TimePeriods.TP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TimePeriods.TP_DESCRIPTION + " TEXT NOT NULL, " +
                "UNIQUE (" + TimePeriods.TP_DESCRIPTION + "))");

        db.execSQL("CREATE TABLE " + Tables.MY_COURSES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MyCoursesColumns.DEPARTMENT_ID + " INTEGER NOT NULL,"
                + MyCoursesColumns.COURSE_ID + " INTEGER NOT NULL,"
                + MyCoursesColumns.SECTION_ID + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.EVENTS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventsColumns.COURSE_ID + " INTEGER NOT NULL,"
                + EventsColumns.TITLE + " TEXT NOT NULL,"
                + EventsColumns.DATE + " TEXT NOT NULL,"
                + EventsColumns.START_TIME + " TEXT NOT NULL,"
                + EventsColumns.END_TIME + " TEXT NOT NULL,"
                + EventsColumns.TYPE_ID + " INTEGER NOT NULL,"
                + EventsColumns.NOTIFICATION + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.EVENT_TYPES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventTypesColumns.TYPE + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOGD(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public long getDepartmentID(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tables.DEPARTMENTS, new String[] {DepartmentsColumns.DEPARTMENT_ID,
            DepartmentsColumns.DEPARTMENT_NAME}, DepartmentsColumns.DEPARTMENT_NAME + "=?",
                new String[] {String.valueOf(name)}, null, null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

//    public ArrayList<String> getDepartmentList() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<String> departments = new ArrayList<>();
//        Cursor cursor = db.query(Tables.DEPARTMENTS, new String[]{DepartmentsColumns.DEPARTMENT_NAME},
//                null, null, null, null, DepartmentsColumns.DEPARTMENT_NAME, null);
//        if(cursor.moveToFirst()) {
//            do {
//                departments.add(cursor.getString(0));
//            } while (cursor.moveToNext());
//        }
//
//        return departments;
//    }

    public SimpleCursorAdapter getDepartmentList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fromColumns = {DepartmentsColumns.DEPARTMENT_NAME};
        int[] toViews = {R.id.spinner_text};

        String query = "SELECT " + DepartmentsColumns.DEPARTMENT_ID + " AS _id, " + DepartmentsColumns.DEPARTMENT_NAME;
        query += " FROM " + Tables.DEPARTMENTS;
        query += " ORDER BY " + DepartmentsColumns.DEPARTMENT_NAME;

        ArrayList<String> departments = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(mContext, R.layout.spinner_item_large,
                cursor, fromColumns, toViews, 0);
        adapter.setDropDownViewResource(R.layout.spinner_item_large);

        return adapter;
    }

    public SimpleCursorAdapter getCoursesForDepartment(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fromColumns = {CoursesColumns.COURSE_NUMBER};
        int[] toViews = {R.id.spinner_text};

        String query = "SELECT " + CoursesColumns.COURSE_ID + " AS _id, " + CoursesColumns.COURSE_NUMBER;
        query += " FROM " + Tables.COURSES;
        query += " WHERE " + CoursesColumns.DEPARTMENT_ID + " = " + id;
        query += " ORDER BY " + CoursesColumns.COURSE_NUMBER;

        Cursor cursor = db.rawQuery(query, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(mContext, R.layout.spinner_item_large,
                cursor, fromColumns, toViews, 0);
        adapter.setDropDownViewResource(R.layout.spinner_item_large);

        return adapter;
    }

    public SimpleCursorAdapter getSectionForDepartment(long deptID, long courseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fromColumns = {"custom_desc"};
        int[] toViews = {R.id.spinner_text};

        String query = "SELECT _id, " + SectionsColumns.SECTION_NUM + " || ' (' || " + TimePeriods.TP_DESCRIPTION + " || ')' AS custom_desc";
        //String query = "SELECT " + SectionsColumns.SECTION_ID + " AS _id, " + SectionsColumns.SECTION_NUM + " || ' (' || " + TimePeriods.TP_DESCRIPTION + " || ')' AS custom_desc";
        query += " FROM " + Tables.SECTIONS + " AS S ";
        query += " JOIN " + Tables.TIME_PERIODS + " AS TP";
        query += " ON S." + SectionsColumns.TP_ID + " = TP." + TimePeriods.TP_ID;
        query += " WHERE S." + SectionsColumns.COURSE_ID + " = " + courseID;
        query += " ORDER BY S." + SectionsColumns.SECTION_NUM;

        ArrayList<String> sections = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(mContext, R.layout.spinner_item_large,
                cursor, fromColumns, toViews, 0);
        adapter.setDropDownViewResource(R.layout.spinner_item_large);

        return adapter;
    }

    public SimpleCursorAdapter getMyCoursesAdapter() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fromColumns = {"custom_desc"};
        int[] toViews = {R.id.spinner_text};

        String query = "SELECT m._id, d." + DepartmentsColumns.DEPARTMENT_NAME + " || ' ' || c." + CoursesColumns.COURSE_NUMBER + " AS custom_desc";
        query += " FROM " + Tables.MY_COURSES + " AS m";
        query += " JOIN " + Tables.DEPARTMENTS + " AS d";
        query += " ON m." + MyCoursesColumns.DEPARTMENT_ID + " = d." + DepartmentsColumns.DEPARTMENT_ID;
        query += " JOIN " + Tables.COURSES + " AS c";
        query += " ON m." + MyCoursesColumns.COURSE_ID + " = c." + CoursesColumns.COURSE_ID;
        query += " JOIN " + Tables.SECTIONS + " AS s";
        query += " ON m." + MyCoursesColumns.SECTION_ID + " = s._id";
        query += " JOIN " + Tables.TIME_PERIODS + " AS tp";
        query += " ON s." + SectionsColumns.TP_ID + " = tp." + TimePeriods.TP_ID;
        query += " JOIN " + Tables.INSTRUCTORS + " AS i";
        query += " ON s." + SectionsColumns.INSTRUCTOR_ID + " = i." + InstructorsColumns.INSTRUCTOR_ID;
        query += " JOIN " + Tables.LOCATIONS + " AS l";
        query += " ON s." + SectionsColumns.LOCATION_ID + " = l." + LocationsColumns.LOCATION_ID;

        Cursor cursor = db.rawQuery(query, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(mContext, R.layout.spinner_item_large,
                cursor, fromColumns, toViews, 0);
        adapter.setDropDownViewResource(R.layout.spinner_item_large);

        return adapter;
    }

    public ArrayList<Course> getMyCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Course> courses = new ArrayList<>();

        String query = "SELECT m._id, d." + DepartmentsColumns.DEPARTMENT_NAME;
        query += ", c." + CoursesColumns.COURSE_NUMBER + ", c." + CoursesColumns.COURSE_NAME;
        query += ", s." + SectionsColumns.SECTION_NUM + ", tp." + TimePeriods.TP_DESCRIPTION;
        query += ", i." + InstructorsColumns.INSTRUCTOR_NAME + ", l." + LocationsColumns.LOCATION_NAME;
        query += " FROM " + Tables.MY_COURSES + " AS m";
        query += " JOIN " + Tables.DEPARTMENTS + " AS d";
        query += " ON m." + MyCoursesColumns.DEPARTMENT_ID + " = d." + DepartmentsColumns.DEPARTMENT_ID;
        query += " JOIN " + Tables.COURSES + " AS c";
        query += " ON m." + MyCoursesColumns.COURSE_ID + " = c." + CoursesColumns.COURSE_ID;
        query += " JOIN " + Tables.SECTIONS + " AS s";
        query += " ON m." + MyCoursesColumns.SECTION_ID + " = s._id";
        query += " JOIN " + Tables.TIME_PERIODS + " AS tp";
        query += " ON s." + SectionsColumns.TP_ID + " = tp." + TimePeriods.TP_ID;
        query += " JOIN " + Tables.INSTRUCTORS + " AS i";
        query += " ON s." + SectionsColumns.INSTRUCTOR_ID + " = i." + InstructorsColumns.INSTRUCTOR_ID;
        query += " JOIN " + Tables.LOCATIONS + " AS l";
        query += " ON s." + SectionsColumns.LOCATION_ID + " = l." + LocationsColumns.LOCATION_ID;

        Cursor cursor = db.rawQuery(query, null);

        System.out.println(query);

        if(cursor.moveToFirst()) {
            do {
                System.out.println("Adding a course");
                Course course = new Course();

                course.setId(cursor.getInt(0));
                course.setDept(cursor.getString(1));
                course.setNumber(cursor.getString(2));
                course.setName(cursor.getString(3));
                course.setSection(cursor.getString(4));
                course.setTime(cursor.getString(5));
                course.setInstructor(cursor.getString(6));
                course.setLocation(cursor.getString(7));

                courses.add(course);
                System.out.println("Course added");
            } while(cursor.moveToNext());
        }

        return courses;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e._id, e." + EventsColumns.TITLE + ", ";
        query += DepartmentsColumns.DEPARTMENT_NAME + " || ' ' || " + CoursesColumns.COURSE_NUMBER + " AS course";
        query += ", e." + EventsColumns.TYPE_ID + ", e." + EventsColumns.DATE;
        query += ", e." + EventsColumns.DATE + ", e." + EventsColumns.START_TIME;
        query += ", e." + EventsColumns.END_TIME + ", e." + EventsColumns.NOTIFICATION;
        query += " FROM " + Tables.EVENTS + " AS e";
        query += " JOIN " + Tables.MY_COURSES + " AS mc";
        query += " ON e." + EventsColumns.COURSE_ID + " = mc._id";
        query += " JOIN " + Tables.DEPARTMENTS + " AS d";
        query += " ON mc." + MyCoursesColumns.DEPARTMENT_ID + " = d." + DepartmentsColumns.DEPARTMENT_ID;
        query += " JOIN " + Tables.COURSES + " AS c";
        query += " ON mc." + MyCoursesColumns.COURSE_ID + " = c." + CoursesColumns.COURSE_ID;
        query += " ORDER BY " + EventsColumns.DATE + ", " + EventsColumns.START_TIME;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Event event = new Event();

                event.setId(cursor.getInt(0));
                event.setName(cursor.getString(1));
                event.setCourse(cursor.getString(2));
                event.setType(cursor.getInt(3));
                event.setDateFromString(cursor.getString(4));
                event.setStartTimeFromString(cursor.getString(5));
                event.setEndTimeFromString(cursor.getString(6));
                event.setNotificationsFromString(cursor.getString(7));

                eventList.add(event);
                System.out.println("Event added");
            } while(cursor.moveToNext());
        }

        return eventList;
    }

    public long getCourseID(int deptID, String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Tables.COURSES, new String[]{CoursesColumns.COURSE_ID,
                        CoursesColumns.DEPARTMENT_ID, CoursesColumns.COURSE_NAME, CoursesColumns.COURSE_NUMBER},
                CoursesColumns.DEPARTMENT_ID + " =? AND " + CoursesColumns.COURSE_NUMBER + " =?",
                new String[]{String.valueOf(deptID), number}, null,
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }
    public long getCourseID(int deptID, String name, String number) {
        System.out.println(deptID + "  " + name + "  " + number);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Tables.COURSES, new String[] {CoursesColumns.COURSE_ID,
            CoursesColumns.DEPARTMENT_ID, CoursesColumns.COURSE_NAME, CoursesColumns.COURSE_NUMBER},
                CoursesColumns.DEPARTMENT_ID + " =? AND " + CoursesColumns.COURSE_NAME + " =? AND "
                + CoursesColumns.COURSE_NUMBER + " =?", new String[] {String.valueOf(deptID), name, number}, null,
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public long getInstructorID(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tables.INSTRUCTORS, new String[] {InstructorsColumns.INSTRUCTOR_ID},
                InstructorsColumns.INSTRUCTOR_NAME + "=?", new String[] {String.valueOf(name)}, null,
                null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public long getLocationID(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tables.LOCATIONS, new String[] {LocationsColumns.LOCATION_ID},
                LocationsColumns.LOCATION_NAME + "=?", new String[] {String.valueOf(name)}, null,
                null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public long getTimePeriodID(String description) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tables.TIME_PERIODS, new String[] {TimePeriods.TP_ID},
                TimePeriods.TP_DESCRIPTION + "=?", new String[] {String.valueOf(description)}, null,
                null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public String getTimePeriodDescription(long tpID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tables.TIME_PERIODS, new String[] {TimePeriods.TP_DESCRIPTION},
                TimePeriods.TP_ID + "=?", new String[] {String.valueOf(tpID)}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        String description = cursor.getString(0);
        cursor.close();
        return description;
    }

    public String getTime(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tables.SECTIONS, new String[]{SectionsColumns.TP_ID},
                SectionsColumns.SECTION_ID + "=?", new String[]{String.valueOf(id)}, null, null,
                null, null);
        System.out.println(cursor.toString());
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            cursor.close();
            return null;
        }

        long tpID = cursor.getLong(0);
        cursor.close();
        return this.getTimePeriodDescription(tpID);
    }

    public String getInstructorForSection(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + InstructorsColumns.INSTRUCTOR_NAME;
        query += " FROM " + Tables.SECTIONS + " AS s";
        query += " JOIN " + Tables.INSTRUCTORS + " AS i";
        query += " ON s." + SectionsColumns.INSTRUCTOR_ID + " = i." + InstructorsColumns.INSTRUCTOR_ID;
        query += " WHERE s._id" + " = " + id;

        System.out.println(query);
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        String instructor = cursor.getString(0);
        cursor.close();
        return instructor;
    }

    public String getLocationForSection(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT l." + LocationsColumns.LOCATION_NAME;
        query += " FROM " + Tables.SECTIONS + " AS s";
        query += " JOIN " + Tables.LOCATIONS + " AS l";
        query += " ON s." + SectionsColumns.LOCATION_ID + " = l." + LocationsColumns.LOCATION_ID;
        query += " WHERE s._id" + " = " + id;

        System.out.println(query);

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        String location = cursor.getString(0);
        cursor.close();
        return location;
    }

    public long insertDepartment(String name) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DepartmentsColumns.DEPARTMENT_NAME, name);

        returnID = db.insertWithOnConflict(Tables.DEPARTMENTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();

        if(returnID == -1) {
            return getDepartmentID(name);
        }

        return returnID;
    }

    public long insertCourse(int deptID, String name, String number) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CoursesColumns.DEPARTMENT_ID, deptID);
        values.put(CoursesColumns.COURSE_NAME, name);
        values.put(CoursesColumns.COURSE_NUMBER, number);

        returnID = db.insertWithOnConflict(Tables.COURSES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();

        if(returnID == -1) {
            return getCourseID(deptID, name, number);
        }

        return returnID;
    }

    public long insertSection(String sectionID, String sectionNum, int courseID, int instructorID, int locationID, int tpID, String credits) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SectionsColumns.SECTION_ID, sectionID);
        values.put(SectionsColumns.SECTION_NUM, sectionNum);
        values.put(SectionsColumns.COURSE_ID, courseID);
        values.put(SectionsColumns.INSTRUCTOR_ID, instructorID);
        values.put(SectionsColumns.LOCATION_ID, locationID);
        values.put(SectionsColumns.TP_ID, tpID);
        values.put(SectionsColumns.CREDITS, credits);

        returnID = db.insertWithOnConflict(Tables.SECTIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        return returnID;
    }

    public long insertInstructor(String name) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InstructorsColumns.INSTRUCTOR_NAME, name);

        returnID = db.insertWithOnConflict(Tables.INSTRUCTORS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();

        if(returnID == -1) {
            return getInstructorID(name);
        }

        return returnID;
    }

    public long insertLocation(String name) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationsColumns.LOCATION_NAME, name);

        returnID = db.insertWithOnConflict(Tables.LOCATIONS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();

        if(returnID == -1) {
            return getLocationID(name);
        }

        return returnID;
    }

    public long insertTimePeriod(String description) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TimePeriods.TP_DESCRIPTION, description);

        returnID = db.insertWithOnConflict(Tables.TIME_PERIODS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();

        if(returnID == -1) {
            return getTimePeriodID(description);
        }

        return returnID;
    }

    public long insertMyCourse(int deptID, int courseID, int sectionID) {
        long returnID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyCoursesColumns.DEPARTMENT_ID, deptID);
        values.put(MyCoursesColumns.COURSE_ID, courseID);
        values.put(MyCoursesColumns.SECTION_ID, sectionID);

        returnID = db.insert(Tables.MY_COURSES, null, values);
        db.close();
        System.out.println("Course inserted " + deptID + " " + courseID + " " + sectionID);

        return returnID;
    }

    public long insertEvent(Event event, int courseID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventsColumns.TITLE, event.getName());
        values.put(EventsColumns.COURSE_ID, courseID);
        values.put(EventsColumns.TYPE_ID, event.getType());
        values.put(EventsColumns.DATE, event.getDate());
        values.put(EventsColumns.START_TIME, event.getStartTime());
        values.put(EventsColumns.END_TIME, event.getEndTime());
        values.put(EventsColumns.NOTIFICATION, event.getNotifications().get(0));
        long returnID = db.insert(Tables.EVENTS, null, values);

        db.close();

        return returnID;
    }
}
