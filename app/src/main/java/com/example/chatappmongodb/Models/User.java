package com.example.chatappmongodb.Models;

import java.util.Date;

public class User {
    private String _id, is_notification, is_muted, is_lastseen, last_seen_date, is_profile, name, email, password, location;
    private Date createdAt, updatedAt;
    private Integer __v;
    private Date passwordResetExpires;
    private String passwordResetToken, theme_color, theme_image, is_status, status, profile, bg_image;


    public User(String is_notification, String is_muted, String is_lastseen, String last_seen_date, String is_profile, String _id, String name, String email, String password, String location, Date createdAt, Date updatedAt, Integer __v, Date passwordResetExpires, String passwordResetToken, String theme_color, String theme_image, String is_status, String status, String profile, String bg_image) {
        this.is_notification = is_notification;
        this.is_muted = is_muted;
        this.is_lastseen = is_lastseen;
        this.last_seen_date = last_seen_date;
        this.is_profile = is_profile;
        this._id = _id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
        this.passwordResetExpires = passwordResetExpires;
        this.passwordResetToken = passwordResetToken;
        this.theme_color = theme_color;
        this.theme_image = theme_image;
        this.is_status = is_status;
        this.status = status;
        this.profile = profile;
        this.bg_image = bg_image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIs_notification() {
        return is_notification;
    }

    public void setIs_notification(String is_notification) {
        this.is_notification = is_notification;
    }

    public String getIs_muted() {
        return is_muted;
    }

    public void setIs_muted(String is_muted) {
        this.is_muted = is_muted;
    }

    public String getIs_lastseen() {
        return is_lastseen;
    }

    public void setIs_lastseen(String is_lastseen) {
        this.is_lastseen = is_lastseen;
    }

    public String getLast_seen_date() {
        return last_seen_date;
    }

    public void setLast_seen_date(String last_seen_date) {
        this.last_seen_date = last_seen_date;
    }

    public String getIs_profile() {
        return is_profile;
    }

    public void setIs_profile(String is_profile) {
        this.is_profile = is_profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public Date getPasswordResetExpires() {
        return passwordResetExpires;
    }

    public void setPasswordResetExpires(Date passwordResetExpires) {
        this.passwordResetExpires = passwordResetExpires;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getTheme_color() {
        return theme_color;
    }

    public void setTheme_color(String theme_color) {
        this.theme_color = theme_color;
    }

    public String getTheme_image() {
        return theme_image;
    }

    public void setTheme_image(String theme_image) {
        this.theme_image = theme_image;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
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

    @Override
    public String toString() {
        return "User{" +
                "is_notification='" + is_notification + '\'' +
                ", is_muted='" + is_muted + '\'' +
                ", is_lastseen='" + is_lastseen + '\'' +
                ", last_seen_date='" + last_seen_date + '\'' +
                ", is_profile='" + is_profile + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", __v=" + __v +
                ", passwordResetExpires=" + passwordResetExpires +
                ", passwordResetToken='" + passwordResetToken + '\'' +
                ", theme_color='" + theme_color + '\'' +
                ", theme_image='" + theme_image + '\'' +
                ", is_status='" + is_status + '\'' +
                ", status='" + status + '\'' +
                ", profile='" + profile + '\'' +
                ", bg_image='" + bg_image + '\'' +
                '}';
    }
}
