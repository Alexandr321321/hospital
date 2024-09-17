package com.example;

public class Service {

    private Integer id;

    private String category;

    private String name;

    private Double cost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Service(Integer id, String category, String name, Double cost) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.cost = cost;
    }

    public Service() {
    }

}
