package com.vikingsen.model.database.comment

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Comment(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        @Column(unique = true)
        var guid: String = "",
        var cheeseId: Long = 0,
        var user: String = "",
        @Column(length = 2048)
        var comment: String = "",
        var created: LocalDate = LocalDate.of(1980, 1, 1)
)