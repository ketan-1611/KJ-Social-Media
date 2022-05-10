package com.example.kjsocialmedia.Models;

public class MessageModel {
    private String message , userId,messageId;
    private long time;

    public MessageModel(String message, String userId, String messageId, long time) {
        this.message = message;
        this.userId = userId;
        this.messageId = messageId;
        this.time = time;
    }

    public MessageModel(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    public MessageModel(String message, String userId, long time) {
        this.message = message;
        this.userId = userId;
        this.time = time;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
