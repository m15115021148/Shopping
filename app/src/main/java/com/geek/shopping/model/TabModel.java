package com.geek.shopping.model;

import java.util.List;


public class TabModel {
    private String name;
    private List<HealthModel> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HealthModel> getData() {
        return data;
    }

    public void setData(List<HealthModel> data) {
        this.data = data;
    }
}
