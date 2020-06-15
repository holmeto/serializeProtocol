package com.gaotu.serialize.model;

import io.protostuff.Tag;

import java.util.List;

public class Person extends Animal{

//    private static final long serialVersionUID = 123456L;

    @Tag(3)
    private String name;

    @Tag(4)
    private int age;

    @Tag(5)
    private int height;

    @Tag(6)
    private String sex;

    @Tag(7)
    private double money;

    @Tag(8)
    private List<String> nickName;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public List<String> getNickName() {
        return nickName;
    }

    public void setNickName(List<String> nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "type='" + getType() + '\'' +
                ", name='" + name + '\'' +
                ", life='" + getLife() + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", sex='" + sex + '\'' +
                ", money=" + money +
                ", nickName=" + nickName +
                '}';
    }
}
