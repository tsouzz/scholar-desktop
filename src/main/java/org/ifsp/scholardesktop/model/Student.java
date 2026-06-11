package org.ifsp.scholardesktop.model;

public class Student {

    private int id;
    private String name;
    private ClassGroup classGroup;

    public Student(String name, ClassGroup classGroup) {
        this.name = name;
        this.classGroup = classGroup;
    }

    public Student(int id, String name, ClassGroup classGroup) {
        this.id = id;
        this.name = name;
        this.classGroup = classGroup;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }
}
