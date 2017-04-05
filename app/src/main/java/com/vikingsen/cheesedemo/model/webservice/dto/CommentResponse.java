package com.vikingsen.cheesedemo.model.webservice.dto;


public class CommentResponse {
    private String guid;
    private int status;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
