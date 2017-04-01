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
}
