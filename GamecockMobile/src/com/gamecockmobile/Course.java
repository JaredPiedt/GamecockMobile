package com.gamecockmobile;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.text.format.Time;

public class Course implements Parcelable {

  private int id;
  private String courseName;
  private ArrayList<ClassTime> classTimes;
  private static int numberOfCourses = 0;

  public Course() {

  }

  public Course(int id, String courseName, ArrayList<ClassTime> classTimes) {
    this.id = id;
    this.courseName = courseName;
    this.classTimes = classTimes;
  }
  
  public Course(int id, String courseName, String classTimes){
    this.id = id;
    this.courseName = courseName;
    setClassTimesFromString(classTimes);
  }
  public Course(String courseName, String classTimes) {
    this.courseName = courseName;
    setClassTimesFromString(classTimes);
  }

  public void setID(int id) {
    this.id = id;
  }

  public int getID() {
    return this.id;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getCourseName() {
    return this.courseName;
  }

  public void setClassTimes(ArrayList<ClassTime> classTimes) {
    this.classTimes = classTimes;
  }

  public ArrayList<ClassTime> getClassTimes() {
    return this.classTimes;
  }

  public void addClassTime(ClassTime classTime) {
    this.classTimes.add(classTime);
  }

  public String classTimesToString(Context context) {
    String s = "";
    ClassTime tempClassTime;

    for (int i = 0; i < classTimes.size(); i++) {
      tempClassTime = classTimes.get(i);
      s += tempClassTime.getDays() + " ";
      s += tempClassTime.getStartTime() + " ";
      s += tempClassTime.getEndTime() + " ";
    }

    return s;
  }

  public void setClassTimesFromString(String string) {
    Scanner scanner = new Scanner(string);
    String token;
    int counter = 1;
    ClassTime tempClassTime;

    while (scanner.hasNext()) {
      token = scanner.next();
      if (token.startsWith("[")) {
        tempClassTime = new ClassTime();
        int end = token.indexOf(",");
        tempClassTime.addDay(token);
        token = token.substring(1, end);
      } else if (token.endsWith("]")) {
        int end = token.indexOf("]");
        token = token.substring(0, end);
        counter++;
      } else if (counter == 2) {
        scanner.nextLong();
        System.out.println("start time");
        counter++;
      } else if (token.startsWith("*") && counter == 3) {
        token = token.substring(1);
        System.out.println("end time");
        counter = 1;
      } else {
        int end = token.indexOf(",");
        token = token.substring(0, end);
      }

    }

  }

  public String toString() {
    String s = "";

    s += courseName.toString();

    return s;
  }

//  public Course createFromParcel(Parcel in) {
//    return new Course(in);
//  }

  @Override
  public int describeContents() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    // TODO Auto-generated method stub
    dest.writeInt(id);
    dest.writeString(courseName);
    dest.writeTypedList(classTimes);
  }
  
  public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
    public Course createFromParcel(Parcel in) {
      return new Course(in);
    }

    @Override
    public Course[] newArray(int size) {
      // TODO Auto-generated method stub
      return new Course[size];
    }
  };

  private Course(Parcel in) {
    id = in.readInt();
    courseName = in.readString();
    classTimes = new ArrayList<ClassTime>();
    in.readTypedList(classTimes, ClassTime.CREATOR);
  }

}
