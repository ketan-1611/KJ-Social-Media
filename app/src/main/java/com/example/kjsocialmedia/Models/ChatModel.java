package com.example.kjsocialmedia.Models;

public class ChatModel {
    private String profile;
    private String name,lastMessage,followerId;

    public ChatModel() {
    }

    public ChatModel(String profile, String name, String lastMessage) {
        this.profile = profile;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
