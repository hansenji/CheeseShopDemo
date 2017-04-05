package com.vikingsen.cheesedemo.model.webservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentRequestDto {
    private String guid;
    private long cheeseId;
    private String user;
    private String comment;

    public CommentRequestDto() {
        // Jackson Constructor
    }

    public CommentRequestDto(String guid, long cheeseId, String user, String comment) {
        this.guid = guid;
        this.cheeseId = cheeseId;
        this.user = user;
        this.comment = comment;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public long getCheeseId() {
        return cheeseId;
    }

    public void setCheeseId(long cheeseId) {
        this.cheeseId = cheeseId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
