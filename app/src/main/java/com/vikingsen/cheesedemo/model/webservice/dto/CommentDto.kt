package com.vikingsen.cheesedemo.model.webservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import org.threeten.bp.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentDto(val guid: String, val cheeseId: Long, val user: String, val comment: String, val created: LocalDateTime)