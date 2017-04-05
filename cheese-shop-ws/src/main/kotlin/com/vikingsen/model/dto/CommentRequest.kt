package com.vikingsen.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentRequest(
        var cheeseId: Long = 0,
        var guid: String = "",
        var user: String = "",
        var comment: String = ""
)