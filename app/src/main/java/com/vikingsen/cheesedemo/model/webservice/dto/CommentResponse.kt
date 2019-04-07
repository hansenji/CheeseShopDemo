package com.vikingsen.cheesedemo.model.webservice.dto

@Suppress("MemberVisibilityCanPrivate")
data class CommentResponse(val guid: String, val status: Int) {
    // Code Created
    val isSuccessful: Boolean
        get() = status == 201
}