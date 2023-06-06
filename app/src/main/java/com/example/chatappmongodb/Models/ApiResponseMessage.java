package com.example.chatappmongodb.Models;

import java.util.ArrayList;

public class ApiResponseMessage {
    private String status, message;
    private ArrayList<Message> listMessage;

    public ApiResponseMessage(String status, String message, ArrayList<Message> listMessage) {
        this.status = status;
        this.message = message;
        this.listMessage = listMessage;
    }

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

    public ArrayList<Message> getListMessage() {
        return listMessage;
    }

    public void setListMessage(ArrayList<Message> listMessage) {
        this.listMessage = listMessage;
    }
}
