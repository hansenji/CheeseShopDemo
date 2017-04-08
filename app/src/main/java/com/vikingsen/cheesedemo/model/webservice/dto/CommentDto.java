package com.vikingsen.cheesedemo.model.webservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.threeten.bp.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {
    private String guid;
    private long cheeseId;
    private String user;
    private String comment;
    private LocalDateTime created;

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
