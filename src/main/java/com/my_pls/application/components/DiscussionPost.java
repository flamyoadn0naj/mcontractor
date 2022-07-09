package com.my_pls.application.components;

public class DiscussionPost {
    private String username;
    private int dgId;
    private String postName;
    private String postContent;
    private String postAttachment;
    private String postDate;

    public int getDgId() {
        return dgId;
    }

    public void setDgId(int dgId) {
        this.dgId = dgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPostAttachment() {
        return postAttachment;
    }

    public void setPostAttachment(String postAttachment) {
        this.postAttachment = postAttachment;
    }

    public DiscussionPost(int dgId, String username, String postName, String postContent, String postDate, String postAttachment){
        this.dgId =dgId;
        this.username =username;
        this.postName =postName;
        this.postContent =postContent;
        this.postDate =postDate;
        this.postAttachment =postAttachment;

    }
}
