package com.gamecockmobile;

import java.util.ArrayList;
import java.util.Scanner;

public class Event {

  private int id;
  private String name;
  private String type;
  private long date;
  private ArrayList<String> notifications;

  public Event() {
    name = "";
    type = "";
    date = 0;
    notifications = new ArrayList<String>();
  }

  public Event(String name, String type, long date, ArrayList<String> notifications) {
    this.name = name;
    this.type = type;
    this.date = date;
    this.notifications = notifications;

  }
  
  public Event(int id, String name, String type, String date, String notificationns){
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public int getId(){
    return this.id;
  }
  
  public void setId(int id){
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getDate() {
    return this.date;
  }

  public void setDate(long date) {
    this.date = date;
  }
  
  public void setDateFromString(String date) {
    Scanner scanner = new Scanner(date);
    
    this.date = scanner.nextLong();
    
    scanner.close();
  }

  public ArrayList<String> getNotifications() {
    return this.notifications;
  }

  public void setNotifications(ArrayList<String> notifications) {
    this.notifications = notifications;
  }
  
  // method only used for testing
  public void setNotificationsFromString(String string){
    Scanner scanner = new Scanner(string);
    
    while(scanner.hasNext()) {
      this.addNotifications(scanner.next());
    }
    
    scanner.close();
  }

  public void addNotifications(String notification) {
    notifications.add(notification);
  }
}
