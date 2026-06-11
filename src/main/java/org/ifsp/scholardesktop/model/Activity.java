package org.ifsp.scholardesktop.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Activity {

    private int id;
    private ActivityType activityType;
    private double grade;
    private LocalDate registrationDate;
    private Student student;

    public Activity(int id, ActivityType activityType, double grade, LocalDate registrationDate, Student student) {
        this.id = id;
        this.activityType = activityType;
        this.grade = grade;
        this.registrationDate = registrationDate;
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Student getStudent() {
        return student;
    }
}
