package com.example.warframes.models;

public class Warframe {
    public String id;
    public String name;
    public String description;
    public String image;


    // Constructor vacío necesario para Firebase
    public Warframe() {
    }

    public Warframe(String id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = url;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getUrl() {
        return image;
    }

}
