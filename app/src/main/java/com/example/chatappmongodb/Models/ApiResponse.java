package com.example.chatappmongodb.Models;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    private String status, message, token;
    private Data data;
    private ArrayList<User> listUser;
    private ArrayList<Message> listMessage;

    /* Constructor*/

    public ApiResponse(String status, String message, String token, Data data) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.data = data;
    }

    public ApiResponse(String status, String message, Data data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String status, String message, ArrayList<Message> listMessage) {
        this.status = status;
        this.message = message;
        this.listMessage = listMessage;
    }

    /* Getter and Setter*/
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ArrayList<User> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<User> listUser) {
        this.listUser = listUser;
    }
}
