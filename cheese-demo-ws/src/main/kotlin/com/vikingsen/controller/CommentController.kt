package com.vikingsen.controller

import com.vikingsen.model.database.cheese.CheeseRepository
import com.vikingsen.model.database.comment.Comment
import com.vikingsen.model.database.comment.CommentRepository
import com.vikingsen.model.dto.CommentRequest
import com.vikingsen.model.dto.CommentResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/comment")
class CommentController(val commentRepository: CommentRepository,
                        val cheeseRepository: CheeseRepository) {

    @GetMapping()
    fun getComments(): Iterable<CommentResponse> {
        return commentRepository.findAll().map { CommentResponse(it.id, it.cheeseId, it.user, it.comment, it.created, it.updated) }
    }

    @GetMapping("/{cheeseId}")
    fun getComments(@PathVariable cheeseId: Long): List<CommentResponse> {
        return commentRepository.findAllByCheeseIdOrderByUpdatedDesc(cheeseId).map { CommentResponse(it.id, it.cheeseId, it.user, it.comment, it.created, it
                .updated) }
    }

    @PostMapping("/{cheeseId}")
    fun postComment(@PathVariable cheeseId: Long, @RequestBody commentRequest: CommentRequest): ResponseEntity<Any> {
        cheeseRepository.findOne(cheeseId).orElseThrow { CheeseNotFoundException() }
        var comment = commentRepository.findByCheeseIdAndUser(cheeseId, commentRequest.user)
        val httpStatus: HttpStatus
        if (comment == null) {
            comment = Comment().apply {
                this.cheeseId = cheeseId
                user = commentRequest.user
                created = LocalDate.now()
            }
            httpStatus = HttpStatus.CREATED
        } else {
            httpStatus = HttpStatus.ACCEPTED
        }
        comment.comment = commentRequest.comment
        comment.updated = LocalDate.now()
        commentRepository.save(comment)
        return ResponseEntity.status(httpStatus).build()
    }
}