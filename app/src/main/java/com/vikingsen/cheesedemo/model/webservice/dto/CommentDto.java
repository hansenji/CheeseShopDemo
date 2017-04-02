package com.vikingsen.cheesedemo.model.webservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.threeten.bp.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {
    private long id;
    private long cheeseId;
    private String user;
    private String comment;
    private LocalDate created;
    private LocalDate updated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }
}
