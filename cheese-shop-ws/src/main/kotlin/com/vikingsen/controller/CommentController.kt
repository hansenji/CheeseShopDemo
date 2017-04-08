package com.vikingsen.controller

import com.vikingsen.model.database.cheese.CheeseRepository
import com.vikingsen.model.database.comment.Comment
import com.vikingsen.model.database.comment.CommentRepository
import com.vikingsen.model.dto.CommentDto
import com.vikingsen.model.dto.CommentRequest
import com.vikingsen.model.dto.CommentResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/comment")
class CommentController(val commentRepository: CommentRepository,
                        val cheeseRepository: CheeseRepository) {

    @GetMapping()
    fun getComments(): Iterable<CommentDto> {
        return commentRepository.findAll().map { CommentDto(it.guid, it.cheeseId, it.user, it.comment, it.created) }
    }

    @PostMapping()
    fun postComments(@RequestBody commentRequests: List<CommentRequest>): List<CommentResponse> {
        return commentRequests.map {
            return@map when {
                commentRepository.existsByGuid(it.guid) -> CommentResponse(it.guid, HttpStatus.CONFLICT.value())
                cheeseRepository.exists(it.cheeseId).not() -> CommentResponse(it.guid, HttpStatus.BAD_REQUEST.value())
                else -> saveComment(it)
            }
        }.toList()
    }

    @GetMapping("/{cheeseId}")
    fun getComments(@PathVariable cheeseId: Long): List<CommentDto> {
        return commentRepository.findAllByCheeseIdOrderByCreatedDesc(cheeseId).map {
            CommentDto(it.guid, it.cheeseId, it.user, it.comment, it.created)
        }
    }

    private fun saveComment(request: CommentRequest): CommentResponse {
        commentRepository.save(Comment().apply {
            this.guid = request.guid
            this.cheeseId = request.cheeseId
            this.user = request.user
            this.comment = request.comment
            this.created = LocalDateTime.now()
        })
        return CommentResponse(request.guid, HttpStatus.CREATED.value())
    }
}