package com.example.kjsocialmedia.Models;

public class PostModel {
    private String postId, postImg,postedBy, postDesc;
    private long postedAt;
    private int postlikes;
    private int commentsCount;

    public PostModel() {
    }

    public PostModel(String postId, String postImg, String postedBy, String postDesc, long postedAt, int postlikes, int commentsCount) {
        this.postId = postId;
        this.postImg = postImg;
        this.postedBy = postedBy;
        this.postDesc = postDesc;
        this.postedAt = postedAt;
        this.postlikes = postlikes;
        this.commentsCount = commentsCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public int getPostlikes() {
        return postlikes;
    }

    public void setPostlikes(int postlikes) {
        this.postlikes = postlikes;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
