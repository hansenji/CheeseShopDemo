
package com.vikingsen.cheesedemo.model.room.cheese

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDateTime


@Entity(tableName = "cheese")
class Cheese  {

    @PrimaryKey
    var id = 0L
    var name = ""
    var description = ""
    var imageUrl = ""
    var cached: LocalDateTime = LocalDateTime.now()


}