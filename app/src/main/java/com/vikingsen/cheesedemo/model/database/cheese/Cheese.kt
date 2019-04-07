package com.vikingsen.cheesedemo.model.database.cheese

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime


@Entity(tableName = "cheese")
data class Cheese(
    @PrimaryKey
    var id: Long = 0L,
    var name: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var sort: Int = -1,
    var cached: LocalDateTime = LocalDateTime.now()
)