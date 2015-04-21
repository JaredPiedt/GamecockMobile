package com.gamecockmobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.JsonReader;

import com.gamecockmobile.provider.ScheduleDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.gamecockmobile.util.LogUtils.LOGE;
import static com.gamecockmobile.util.LogUtils.makeLogTag;

/**
 * Created by piedt on 2/28/15.
 */
public class CourseDatabaseService extends IntentService {
    private ScheduleDatabase mDB = new ScheduleDatabase(this);
    private static final String TAG = makeLogTag(CourseDatabaseService.class);

    private static final String INSTRUCTORS = "instructors";
    private static final String NUMBER = "number";
    private static final String CREDITS = "credits";
    private static final String NAME = "name";
    private static final String SECTION = "section";
    private static final String DAYS = "days";
    private static final String DEPT = "dept";
    private static final String LOCATION = "location";
    private static final String TIME = "time";
    private static final String IDENTIFIER = "identifier";

    private static final String URL = "http://jmvidal.cse.sc.edu/schedule/schedule.json";

    public CourseDatabaseService(){
        super("CourseDatabaseService");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CourseDatabaseService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InputStream stream = retrieveStream();

        JsonReader reader = new JsonReader(new InputStreamReader(stream));

        try {
            parseClassesArray(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream retrieveStream() {
        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet getRequest = new HttpGet(URL);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode != HttpStatus.SC_OK) {
                LOGE(TAG, "Error " + statusCode + " for URL " + URL);
                return null;
            }

            HttpEntity entity = response.getEntity();
            return entity.getContent();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseClassesArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while(reader.hasNext()) {
            parseJson(reader);
        }
        reader.endArray();
        System.out.println("******* finished reading");
    }
    private void parseJson(JsonReader reader) throws IOException {
        System.out.println("******** parsing json");
        String instructor = null;
        String number = null;
        String credits = null;
        String courseName = null;
        String section = null;
        String day = null;
        String dept = null;
        String location = null;
        String time = null;
        String identifier = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if(name.equals(INSTRUCTORS)) {
                instructor = reader.nextString();
            } else if(name.equals(NUMBER)) {
                number = reader.nextString();
            } else if(name.equals(CREDITS)) {
                credits = reader.nextString();
            } else if(name.equals(NAME)) {
                courseName = reader.nextString();
                System.out.println("***********" + courseName);
            } else if(name.equals(SECTION)) {
                section = reader.nextString();
            } else if(name.equals(DAYS)) {
                day = reader.nextString();
            } else if(name.equals(DEPT)) {
                dept = reader.nextString();
            } else if(name.equals(LOCATION)) {
                location = reader.nextString();
            } else if(name.equals(TIME)) {
                time = reader.nextString();
            } else if(name.equals(IDENTIFIER)) {
                identifier = reader.nextString();
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        long deptID = mDB.insertDepartment(dept);
        System.out.println("******* dept id = " + deptID);

        long courseID = mDB.insertCourse((int) deptID, courseName, number);
        System.out.println("******* course id = " + courseID);

        long instructorID = mDB.insertInstructor(instructor);

        long locationID = mDB.insertLocation(location);

        long tpID = -1;

        if(day != null) {
            tpID = mDB.insertTimePeriod(day + " " + time);
        } else {
            tpID = mDB.insertTimePeriod(time);
        }

        long sectionID = -1;
        String t = mDB.getTime(identifier);

        if(t == null) {
            sectionID = mDB.insertSection(identifier, section, (int) courseID, (int) instructorID, (int) locationID, (int) tpID, credits);
            System.out.println("****** success!!!");
        } else {
            System.out.println("***** multiple times -> " + identifier);
            String description = t + ", " + day + " " + time;
            System.out.println("***** new time = " + description);
            System.out.println("***** tpID before = "  + tpID);
            tpID = mDB.insertTimePeriod(description);
            System.out.println("***** tpID after = " + tpID);
            sectionID = mDB.insertSection(identifier, section, (int) courseID, (int) instructorID, (int) locationID, (int) tpID, credits);
        }
    }
}
