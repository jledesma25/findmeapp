package com.imast.findingme.model;

/**
 * Created by lmcornejov on 08/08/2015.
 */
public class Race {

    private int id;
    private String name;

    public Race(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return this.name;
    }
}
