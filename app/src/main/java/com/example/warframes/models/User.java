package com.example.warframes.models;

public class User {
    public String uid;
    public String name;
    public String email;
    public String telephone;
    public String address;

    // Constructor vacío necesario para Firebase
    public User() {
    }

    public User(String uid, String name, String email, String telephone, String address) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getTelephone() {
        return telephone;
    }
    public String getAddress() {
        return address;
    }
}