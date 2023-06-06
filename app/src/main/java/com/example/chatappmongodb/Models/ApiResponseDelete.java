package com.example.chatappmongodb.Models;

import java.util.ArrayList;

public class ApiResponseDelete {
    private String status, message;
    private int deletedCount;

    public ApiResponseDelete(String status, String message, int deletedCount) {
        this.status = status;
        this.message = message;
        this.deletedCount = deletedCount;
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

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }
}
