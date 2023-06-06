package com.example.chatappmongodb.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    List<User> users;
    User user;
    String email, name, status, profile, bg_image;

    public Data(String email, String name, String status, String profile, String bg_image) {
        this.email = email;
        this.name = name;
        this.status = status;
        this.profile = profile;
        this.bg_image = bg_image;
    }

    public Data(List<User> users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBg_image() {
        return bg_image;
    }

    public void setBg_image(String bg_image) {
        this.bg_image = bg_image;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
