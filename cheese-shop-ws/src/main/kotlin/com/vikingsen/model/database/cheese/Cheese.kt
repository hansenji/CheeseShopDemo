package com.vikingsen.model.database.cheese

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Cheese(
        var name: String = "",
        var image: String = "",
        @Column(length = 2048)
        var description: String = "",
        var sort: Int = 0,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0
)