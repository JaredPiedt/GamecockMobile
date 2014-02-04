package com.gamecockmobile;

public class ClassTime {

  private String days;
  private String startTime;
  private String endTime;
  
  public ClassTime(){
    
  }
  
  public ClassTime(String days, String startTime, String endTime) {
    this.days = days;
    this.startTime = startTime;
    this.endTime = endTime;
  }
  
  public void setDays(String days){
    this.days = days;
  }
  
  public String getDays() {
    return this.days;
  }
  
  public void setStartTime(String startTime){
    this.startTime = startTime;
  }
  
  public String getStartTime(){
    return this.startTime;
  }
  
  public void setEndTime(String endTime){
    this.endTime = endTime;
  }
  
  public String getEndTime(){
    return this.endTime;
  }
}
