package com.vikingsen.cheesedemo.model.webservice.dto

import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

data class CommentDto(
    val guid: String,
    val cheeseId: Long,
    val user: String,
    val comment: String,
    val created: OffsetDateTime = OffsetDateTime.of(1980, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
)