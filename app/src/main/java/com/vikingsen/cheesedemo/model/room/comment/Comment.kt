package com.vikingsen.cheesedemo.model.room.comment

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDateTime


@Entity(tableName = "comment")
class Comment  {
    @PrimaryKey
    var id = ""
    var cheeseId = 0L
    var user = ""
    var comment = ""
    var created: LocalDateTime = LocalDateTime.now()
    var synced = false
    var cached: LocalDateTime? = null


}