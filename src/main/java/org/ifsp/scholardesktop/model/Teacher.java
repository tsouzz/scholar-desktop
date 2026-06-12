package org.ifsp.scholardesktop.model;

public class Teacher {

    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private School school;

    public Teacher(String name, String email, String password, School school) {
        this.name = name;
        this.email = email;
        this.passwordHash = password;
        this.school = school;
    }

    public Teacher(int id, String name, String email, String password, School school) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = password;
        this.school = school;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
