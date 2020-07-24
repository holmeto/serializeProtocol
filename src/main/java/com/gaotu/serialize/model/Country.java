package com.gaotu.serialize.model;

import java.util.List;

public class Country {

    private String name;

    private List<City> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCities() {
        return cities;
    }

    public static String test(int value) {
        return String.valueOf(value);
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", cities=" + cities +
                '}';
    }
}
