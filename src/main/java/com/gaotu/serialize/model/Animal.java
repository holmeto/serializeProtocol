package com.gaotu.serialize.model;

import io.protostuff.Tag;

public class Animal {


    @Tag(1)
    private String type;

    @Tag(2)
    private int life;

    public Animal() {
    }

    public Animal(String type, int life) {
        this.type = type;
        this.life = life;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
