package com.vikingsen.cheesedemo.model.webservice.dto


data class CommentRequestDto(val guid: String, val cheeseId: Long, val user: String, val comment: String)