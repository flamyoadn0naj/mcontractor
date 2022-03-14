package com.my_pls;

public class DiscussionPost {
    public int getDgId() {
        return dgId;
    }

    public void setDgId(int dgId) {
        this.dgId = dgId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    int userId;
    int dgId;

    String postName;
    String postContent;

    String postDate;

    public DiscussionPost(int dgId, int userId, String postName, String postContent, String postDate){
        this.dgId =dgId;
        this.userId =userId;
        this.postName =postName;
        this.postContent =postContent;
        this.postDate =postDate;

    }
}
