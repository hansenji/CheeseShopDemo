package com.vikingsen.cheesedemo.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.cheese.CheeseDao
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.database.comment.CommentDao

@Database(
    entities = [
        Cheese::class,
        Comment::class
    ],
    version = 3
)
@TypeConverters(DateTimeConverters::class)
abstract class ShopDatabase : RoomDatabase() {
    abstract val cheeseDao: CheeseDao
    abstract val commentDao: CommentDao

    companion object {
        const val DATABASE_NAME = "shop.db"

        val migrations = arrayOf<Migration>()
    }
}