package com.gaotu.serialize.model;

import io.protostuff.Tag;

public class Person {

//    private static final long serialVersionUID = 123456L;

    @Tag(1)
    private String name;

    @Tag(2)
    private int age;

    @Tag(5)
    private int height;

    @Tag(3)
    private String sex;

    @Tag(4)
    private double money;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
//                ", height=" + height +
                ", sex='" + sex + '\'' +
                ", money=" + money +
                '}';
    }
}
