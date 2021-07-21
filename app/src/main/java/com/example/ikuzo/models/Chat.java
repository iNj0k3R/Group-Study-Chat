package com.example.ikuzo.models;

public class Chat {
    private String sender;
    private String message;
    private String senderName;

    private String imageUrl;

    public Chat(String sender, String message, String senderName) {
        this.sender = sender;
        this.message = message;
        this.senderName = senderName;
        this.imageUrl = null;
    }
    public Chat(String sender, String message, String senderName, String imageUrl) {
        this.sender = sender;
        this.message = message;
        this.senderName = senderName;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
