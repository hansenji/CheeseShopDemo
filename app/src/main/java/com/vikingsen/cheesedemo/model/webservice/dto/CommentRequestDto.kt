package com.vikingsen.cheesedemo.model.webservice.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentRequestDto(val guid: String, val cheeseId: Long, val user: String, val comment: String)