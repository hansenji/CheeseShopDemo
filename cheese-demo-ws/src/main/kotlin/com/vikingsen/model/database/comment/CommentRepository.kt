package com.vikingsen.model.database.comment

import org.springframework.data.repository.CrudRepository

interface CommentRepository: CrudRepository<Comment, Long> {
    fun findAllByCheeseIdOrderByCreatedDesc(cheeseId: Long): List<Comment>
    fun findByGuid(guid: String): Comment?
    fun existsByGuid(guid: String): Boolean
}