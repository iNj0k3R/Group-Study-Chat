package com.example.ikuzo.models;

public class Chat {
    private String sender;
    private String message;
    private String senderName;

    public Chat(String sender, String message, String senderName) {
        this.sender = sender;
        this.message = message;
        this.senderName = senderName;
    }
    public Chat(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
