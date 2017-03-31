package com.vikingsen.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentResponse(
        val id: Long,
        val cheeseId: Long,
        val user: String,
        val comment: String,
        val created: LocalDate,
        val updated: LocalDate
)
