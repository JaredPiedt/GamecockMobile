package com.gamecockmobile;

import java.util.ArrayList;

public class Event {

  private String name;
  private String type;
  private long date;
  private ArrayList<String> notifications;
  
  public Event(){
    name = "";
    type = "";
    date = 0;
    notifications = new ArrayList<String>();
  }
  
  public Event(String name, String type, long date, ArrayList<String> notifications){
    this.name = name;
    this.type = type;
    this.date = date;
    this.notifications = notifications;
  }
  
  public String getName(){
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }
  
  public String getType(){
    return this.type;
  }
  
  public void setType(String type){
    this.type = type;
  }
  
  public long getDate(){
    return this.date;
  }
  
  public void setDate(long date){
    this.date = date;
  }
  
  public ArrayList<String> getNotifications(){
    return this.notifications;
  }
  
  public void setNotifications(ArrayList<String> notifications){
    this.notifications = notifications;
  }
  
  public void addNotifications(String notification){
    notifications.add(notification);
  }
}
