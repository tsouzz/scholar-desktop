package org.ifsp.scholardesktop.model;

public class ClassGroup {

    private int id;
    private String name;
    private int number;
    private Module module;
    private Teacher teacher;

    public ClassGroup(String name, int number, Module module, Teacher teacher) {
        this.name = name;
        this.number = number;
        this.module = module;
        this.teacher = teacher;
    }

    public ClassGroup(int id, String name, int number, Module module, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.module = module;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return module.getAcronym() + "-" + String.format("%04d", number);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
