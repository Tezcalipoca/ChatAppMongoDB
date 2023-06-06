package com.example.chatappmongodb.Models;

import java.util.Date;

public class Message {
    String message, has_dropDown, has_images, has_files, has_audio, contacts_id, unread, location, voice_recoder, flag, sender_id, receiver_id;
    Date createdAt, updateAt;
    int __v;

    public Message(String message, String has_dropDown, String has_images, String has_files, String has_audio, String contacts_id, String unread, String location, String voice_recoder, String flag, String sender_id, String receiver_id, Date createdAt, Date updateAt, int __v) {
        this.message = message;
        this.has_dropDown = has_dropDown;
        this.has_images = has_images;
        this.has_files = has_files;
        this.has_audio = has_audio;
        this.contacts_id = contacts_id;
        this.unread = unread;
        this.location = location;
        this.voice_recoder = voice_recoder;
        this.flag = flag;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.__v = __v;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHas_dropDown() {
        return has_dropDown;
    }

    public void setHas_dropDown(String has_dropDown) {
        this.has_dropDown = has_dropDown;
    }

    public String getHas_images() {
        return has_images;
    }

    public void setHas_images(String has_images) {
        this.has_images = has_images;
    }

    public String getHas_files() {
        return has_files;
    }

    public void setHas_files(String has_files) {
        this.has_files = has_files;
    }

    public String getHas_audio() {
        return has_audio;
    }

    public void setHas_audio(String has_audio) {
        this.has_audio = has_audio;
    }

    public String getContacts_id() {
        return contacts_id;
    }

    public void setContacts_id(String contacts_id) {
        this.contacts_id = contacts_id;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVoice_recoder() {
        return voice_recoder;
    }

    public void setVoice_recoder(String voice_recoder) {
        this.voice_recoder = voice_recoder;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
