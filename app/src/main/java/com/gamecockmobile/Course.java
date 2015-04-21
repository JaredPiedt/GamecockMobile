package com.gamecockmobile;

/**
 * Created by piedt on 4/18/15.
 */
public class Course {

    private int id;
    private String dept;
    private String number;
    private String name;
    private String section;
    private String time;
    private String instructor;
    private String location;

    public Course(){

    }

    public Course(int id, String dept, String number, String name, String section, String time, String instructor, String location) {
        this.id = id;
        this.dept = dept;
        this.number = number;
        this.name = name;
        this.section = section;
        this.time = time;
        this.instructor = instructor;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept() {
        return this.dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return this.section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
