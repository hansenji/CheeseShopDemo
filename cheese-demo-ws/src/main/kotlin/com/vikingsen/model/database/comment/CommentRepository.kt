package com.vikingsen.model.database.comment

import org.springframework.data.repository.CrudRepository

interface CommentRepository: CrudRepository<Comment, Long> {
    fun findAllByCheeseIdOrderByUpdatedDesc(cheeseId: Long): List<Comment>
    fun findByCheeseIdAndUser(cheeseId: Long, user: String): Comment?
}