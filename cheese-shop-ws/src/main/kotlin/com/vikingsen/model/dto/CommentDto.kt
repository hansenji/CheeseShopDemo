package com.vikingsen.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentDto(
        val guid: String,
        val cheeseId: Long,
        val user: String,
        val comment: String,
        val created: LocalDateTime
)
