package com.gamecockmobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

public class ClassTime implements Parcelable {

  private int id;
  private CharSequence[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
  private ArrayList<CharSequence> selectedDays = new ArrayList<CharSequence>();
  private long startTime;
  private long endTime;

  public ClassTime() {

  }

  public ClassTime(ArrayList<CharSequence> days, long startTime, long endTime) {
    this.selectedDays = days;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public void setID(int id) {
    this.id = id;
  }
  
  public int getID(){
    return this.id;
  }

  public void setDays(ArrayList<CharSequence> days) {
    this.selectedDays = days;
  }

  public void setDaysFromString(String s) {
    System.out.println("enter setDaysFromString");
    int end = s.indexOf("]");
    s = s.substring(1, end);
    List<String> sdays = Arrays.asList(s.split(","));
    for (String string : sdays) {
      System.out.println(string);
      this.addDay(string);
    }

  }

  public ArrayList<CharSequence> getDays() {
    return selectedDays;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public String getStartTimeAsString(Context context) {
    String timeString;
    int flags = DateUtils.FORMAT_SHOW_TIME;
    flags |= DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
    timeString = DateUtils.formatDateTime(context, startTime, flags);
    return timeString;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public long getEndTime() {
    return this.endTime;
  }

  public String getEndTimeAsString(Context context) {
    String timeString;
    int flags = DateUtils.FORMAT_SHOW_TIME;
    flags |= DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
    timeString = DateUtils.formatDateTime(context, endTime, flags);
    return timeString;
  }

  public void addDay(String s) {
    System.out.println("enter addDay");
    System.out.println(s);
    for (CharSequence ch : days) {
      System.out.println(ch);
      if (s.equals(ch)) {
        selectedDays.add(ch);
        System.out.println(ch + "was added");
      }
    }
  }

  @Override
  public int describeContents() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flag) {
    // TODO Auto-generated method stub
    dest.writeInt(id);
    dest.writeList(selectedDays);
    dest.writeLong(startTime);
    dest.writeLong(endTime);
  }

  public static final Parcelable.Creator<ClassTime> CREATOR = new Parcelable.Creator<ClassTime>() {
    public ClassTime createFromParcel(Parcel in) {
      return new ClassTime(in);
    }

    @Override
    public ClassTime[] newArray(int size) {
      // TODO Auto-generated method stub
      return new ClassTime[size];
    }
  };

  private ClassTime(Parcel in) {
    id = in.readInt();
    in.readList(selectedDays, CharSequence.class.getClassLoader());
    startTime = in.readLong();
    endTime = in.readLong();
  }

  public void stringToDays(String s) {
    int end = s.indexOf("]");
    s = s.substring(1, end);
    List<String> sdays = Arrays.asList(s.split(","));
    for (String string : sdays) {
      this.addDay(string);
    }

  }

}
