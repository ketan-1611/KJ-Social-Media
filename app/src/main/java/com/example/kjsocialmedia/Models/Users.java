package com.example.kjsocialmedia.Models;

public class Users {
    private String username,profession,email,password;
    private String coverPhotos,profile,userId;
    private int followerCount;

    public Users(String username, String profession, String email, String password, String coverPhotos, String profile, String userId, int followerCount) {
        this.username = username;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhotos = coverPhotos;
        this.profile = profile;
        this.userId = userId;
        this.followerCount = followerCount;
    }

    public Users(String username, String profession, String email, String password) {
        this.username = username;
        this.profession = profession;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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

    public String getCoverPhotos() {
        return coverPhotos;
    }

    public void setCoverPhotos(String coverPhotos) {
        this.coverPhotos = coverPhotos;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
