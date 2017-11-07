package com.vikingsen.cheesedemo.model.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.vikingsen.cheesedemo.model.room.cheese.Cheese
import com.vikingsen.cheesedemo.model.room.cheese.CheeseDao
import com.vikingsen.cheesedemo.model.room.comment.Comment
import com.vikingsen.cheesedemo.model.room.comment.CommentDao


@Database(entities = arrayOf(
        Cheese::class,
        Comment::class), version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun cheeseDao(): CheeseDao
    abstract fun commentDao(): CommentDao

    companion object {
        val DATABASE_NAME: String = "shop.sqlite"
    }
}