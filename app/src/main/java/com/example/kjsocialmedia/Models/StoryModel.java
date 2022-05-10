package com.example.kjsocialmedia.Models;

import java.util.ArrayList;

public class StoryModel {
    private String storyBy;
    private long storyAt;
    private ArrayList<UserStories> stories;

    public StoryModel() {
    }

    public StoryModel(String storyBy, long storyAt, ArrayList<com.example.kjsocialmedia.Models.UserStories> stories) {
        this.storyBy = storyBy;
        this.storyAt = storyAt;
        this.stories = stories;
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<com.example.kjsocialmedia.Models.UserStories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<com.example.kjsocialmedia.Models.UserStories> stories) {
        this.stories = stories;
    }
}
