package com.example.viniciusmn.events.Classes;

import java.io.Serializable;

public class Person implements Serializable{
    private String name;
    private boolean confirmed;

    public Person(String name) {
        this.name = name;
        this.confirmed = false;
    }

    public Person(String name, boolean confirmed) {
        this.name = name;
        this.confirmed = confirmed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", confirmed=" + confirmed +
                '}';
    }
}
