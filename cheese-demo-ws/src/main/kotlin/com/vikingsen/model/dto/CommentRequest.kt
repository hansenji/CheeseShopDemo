package com.vikingsen.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentRequest(
        var user: String = "",
        var comment: String = ""
)