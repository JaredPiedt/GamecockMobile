package com.gamecockmobile.events;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * Class used to set up an Event.
 * 
 * @author Jared W. Piedt
 * 
 */
public class Event {

  /****************************************************************
   * Instance variables.
   */
  private int id;
  private String name;
  private String course;
  private int type;
  private long date;
  private long startTime;
  private long endTime;
  private ArrayList<Integer> notifications;

  /****************************************************************
   * Default constructor.
   */
  public Event() {
    name = "";
    course = "";
    type = 0;
    date = 0;
    startTime = 0;
    endTime = 0;
    notifications = new ArrayList<Integer>();
  }

  /****************************************************************
   * Constructor used in the 'EventDatabaseHandler' class.
   */
  public Event(int id, String name, String course, String type, String date, String startTime, String endTime, String notifications) {
    this.id = id;
    this.name = name;
    this.course = course;
    this.type = Integer.valueOf(type);
    this.date = Long.valueOf(date);
    this.startTime = Long.valueOf(startTime);
    this.endTime = Long.valueOf(endTime);
    this.notifications = new ArrayList<Integer>();
    this.setNotificationsFromString(notifications);

  }

  public Event(int id, String name, String course, int type, String date, String notifications) {
    this.id = id;
    this.name = name;
    this.course = course;
    this.type = type;
  }

  /****************************************************************
   * Accessors and mutators.
   */
  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCourse() {
    return this.course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public int getType() {
    return this.type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public long getDate() {
    return this.date;
  }

  public void setDate(long date) {
    this.date = date;
  }
  
  public long getStartTime() {
    return this.startTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  public long getEndTime() {
    return this.endTime;
  }
  
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }  

  public void setDateFromString(String date) {
    Scanner scanner = new Scanner(date);

    this.date = scanner.nextLong();

    scanner.close();
  }
  
  public String getStartTimeAsString(Context context) {
    String timeString;
    int flags = DateUtils.FORMAT_SHOW_TIME;
    flags |= DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
    timeString = DateUtils.formatDateTime(context, startTime, flags);
    return timeString;
  }
  
  public String getEndTimeAsString(Context context) {
    String timeString;
    int flags = DateUtils.FORMAT_SHOW_TIME;
    flags |= DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
    timeString = DateUtils.formatDateTime(context, endTime, flags);
    return timeString;
  }
  
  public void setStartTimeFromString(String startTime) {
    Scanner scanner = new Scanner(startTime);
    
    this.startTime = scanner.nextLong();
    
    scanner.close();    
  }
  
  public void setEndTimeFromString(String endTime){
    Scanner scanner = new Scanner(endTime);
    
    this.endTime = scanner.nextLong();
    
    scanner.close();
  }

  public ArrayList<Integer> getNotifications() {
    return this.notifications;
  }

  public void setNotifications(ArrayList<Integer> notifications) {
    this.notifications = notifications;
  }

  // method only used for testing
  public void setNotificationsFromString(String string) {
    Scanner scanner = new Scanner(string);

    while (scanner.hasNext()) {
      this.addNotification(scanner.nextInt());
    }

    scanner.close();
  }

  /****************************************************************
   * Method to add a single notification.
   * 
   * @param notification
   *          the notification to add
   */
  public void addNotification(int notification) {
    System.out.println(notification);
    this.notifications.add(notification);

  }

  /****************************************************************
   * Method to compare two events.
   * 
   * We compare based on the data.
   * 
   */
  public int compareTo(Event e) {
    if (this.date == e.getDate()) {
      if(this.startTime == e.startTime){
        return 0;
      }
      else if(this.startTime > e.startTime) {
        return -1;
      }
      else {
        return 1;
      }
    } else if (this.date > e.getDate()) {
      return -1;
    } else {
      return 1;
    }
  }

  /****************************************************************
   * Method to test two events for equality.
   * 
   * @param e
   *          the Event you want to compare
   * @return the usual -1, 0, +1 for a comparison
   */
  public boolean equals(Object e) {
    return 0 == this.compareTo((Event) e);
  }
  
  /****************************************************************
   * Basic 'toString' for the class.
   */
  public String toString() {
    String s = "";
    s += this.name;
    s += " " + this.course;
    s += " " + this.date;
    s += " " + this.startTime;
    s += " " + this.endTime;
    s += " " + this.type;
    s += " " + this.notifications.toString();
    return s;
  }
}
