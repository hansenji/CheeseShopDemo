package com.vikingsen.cheesedemo.model.webservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Suppress("MemberVisibilityCanPrivate")
@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentResponse(val guid: String, val status: Int) {
    // Code Created
    val isSuccessful: Boolean
        get() = status == 201
}
