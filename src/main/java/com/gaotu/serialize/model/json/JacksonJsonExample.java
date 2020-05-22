package com.gaotu.serialize.model.json;

public class JacksonJsonExample {

    private String name;

    private int age;

    private String sex;

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

    public JacksonJsonExample() {
    }

    public JacksonJsonExample(String name, int age, String sex, double money) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.money = money;
    }

    @Override
    public String toString() {
        return "JacksonJsonExample{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", money=" + money +
                '}';
    }
}
