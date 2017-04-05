package com.vikingsen.model.database.price

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Price(
        var cheeseId: Long = 0,
        var price: Double = 0.0,
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
)