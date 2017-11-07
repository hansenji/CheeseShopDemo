package com.vikingsen.cheesedemo.model.data.comment


class CommentChange private constructor(val cheeseId: Long, val isBulkChange: Boolean) {
    companion object {
        fun bulkOperation(): CommentChange {
            return CommentChange(-1, true)
        }

        fun forCheese(cheeseId: Long): CommentChange {
            return CommentChange(cheeseId, false)
        }
    }
}
