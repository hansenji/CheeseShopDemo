package com.vikingsen.cheesedemo.model.database.comment

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey
    var id: String = "",
    var cheeseId: Long = 0L,
    var user: String = "",
    var comment: String = "",
    var created: OffsetDateTime = OffsetDateTime.now(),
    var synced: Boolean = false,
    var cached: LocalDateTime? = null
)