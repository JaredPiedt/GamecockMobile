package com.gamecockmobile;

import java.util.ArrayList;

public class Course {

  private String courseName;
  private ArrayList<ClassTime> classTimes;
  
  public Course(){
    
  }
  
  public Course(String courseName, ArrayList<ClassTime> classTimes){
    this.courseName = courseName;
    this.classTimes = classTimes;
  }
  
  public void setCourseName(String courseName){
    this.courseName = courseName;
  }
  
  public String getCourseName(){
    return this.courseName;
  }
  
  public void setClassTimes(ArrayList<ClassTime> classTimes){
    this.classTimes = classTimes;
  }
  
  public ArrayList<ClassTime> getClassTimes(){
    return this.classTimes;
  }
  
  public void addClassTime(ClassTime classTime){
    this.classTimes.add(classTime);
  }
  
  public String toString(){
    String s = "";
    
    s += courseName.toString();
    
    return s;
  }
  
}
