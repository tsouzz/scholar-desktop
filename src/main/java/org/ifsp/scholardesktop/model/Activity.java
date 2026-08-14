package org.ifsp.scholardesktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Activity {

    private int id;
    private ActivityType activityType;
    private BigDecimal grade;
    private LocalDate registrationDate;
    private Student student;

    public Activity(int id, ActivityType activityType, BigDecimal grade, LocalDate registrationDate, Student student) {
        this.id = id;
        this.activityType = activityType;
        this.grade = grade;
        this.registrationDate = registrationDate;
        this.student = student;
    }

    public Activity(ActivityType activityType, BigDecimal grade, LocalDate registrationDate, Student student) {
        this.activityType = activityType;
        this.grade = grade;
        this.registrationDate = registrationDate;
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
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
