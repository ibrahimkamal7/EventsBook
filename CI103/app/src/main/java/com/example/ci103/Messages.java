package com.example.ci103;

public class Messages {
    private String message;
    private String senderId;
    private String id;
    private String time;

    public Messages(String message, String senderId,String id,String time) {
        this.message = message;
        this.senderId = senderId;
        this.id=id;
        this.time=time;
    }

    public Messages() {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
