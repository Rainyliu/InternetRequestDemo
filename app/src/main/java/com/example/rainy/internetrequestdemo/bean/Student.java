package com.example.rainy.internetrequestdemo.bean;

import java.util.List;

/**
 * Author: Rainy <br>
 * Description: InternetRequestDemo <br>
 * Since: 2016/11/25 0025 下午 2:33 <br>
 */

public class Student {
    private String name;
    private List<Course> courses;


    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
