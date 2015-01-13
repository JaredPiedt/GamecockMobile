package com.gamecockmobile;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Class that is used to set up a Course.
 * 
 * @author Jared Piedt
 *
 */
public class Course implements Parcelable {

  /**
   * Instance variables.
   */
  private int id;
  private String courseName;
  private ArrayList<ClassTime> classTimes;

  /**
   * Default constructor.
   */
  public Course() {
    this.id = 0;
    this.courseName = "";
    classTimes = new ArrayList<ClassTime>();
  }

  /**
   * Constructor.
   * 
   * @param id
   * @param courseName
   * @param classTimes
   */
  public Course(int id, String courseName, ArrayList<ClassTime> classTimes) {
    this.id = id;
    this.courseName = courseName;
    this.classTimes = classTimes;
  }

  /**
   * Constructor.
   * 
   * @param id
   * @param courseName
   * @param classTimes
   */
  public Course(int id, String courseName, String classTimes) {
    this.id = id;
    this.courseName = courseName;
    setClassTimesFromString(classTimes);
  }

  /**
   * Constructor.
   * 
   * @param courseName
   * @param classTimes
   */
  public Course(String courseName, String classTimes) {
    this.courseName = courseName;
    setClassTimesFromString(classTimes);
  }

  /**
   * Accessors and mutators.
   * 
   * @param id
   */
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
      s += tempClassTime.getID() + " ";
      s += tempClassTime.getDays() + " ";
      s += tempClassTime.getStartTime() + " ";
      s += tempClassTime.getEndTime() + " ";
    }

    return s;
  }
  
  public void removeClassTime(int id)
  {
    for(int i = 0; i < this.classTimes.size(); i++)
    {
      if(this.classTimes.get(i).getID() == id)
      {
        this.classTimes.remove(i);
      }
    }
  }

  public void setClassTimesFromString(String string) {

    Scanner scanner = new Scanner(string);
    String token;
    ClassTime tempClassTime;

    classTimes = new ArrayList<ClassTime>();
    while (scanner.hasNext()) {
      tempClassTime = new ClassTime();
      tempClassTime.setID(scanner.nextInt());
      String s = "";
      while (!(token = scanner.next()).contains("]")) {
        s += token;
        Log.d("token", token);
      }
      s += token;
      Log.d("token", token);
      System.out.println(s);
      tempClassTime.setDaysFromString(s);
      tempClassTime.setStartTime(scanner.nextLong());
      // Log.d("start time", Long.toString(scanner.nextLong()));
      tempClassTime.setEndTime(scanner.nextLong());
      // Log.d("end time", Long.toString(scanner.nextLong()));
      classTimes.add(tempClassTime);
    }
    scanner.close();

  }

  public String toString() {
    String s = "";

    s += courseName.toString();

    for (int i = 0; i < classTimes.size(); i++) {
      classTimes.get(i).getDays().toString();
    }

    return s;
  }

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
