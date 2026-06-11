package org.ifsp.scholardesktop.model;

public class School {

    private int id;
    private String name;

    public School(String name) {
        this.name = name;
    }

    public School(int id, String name) {
        this.id = id;
        this.name = name;
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
}
