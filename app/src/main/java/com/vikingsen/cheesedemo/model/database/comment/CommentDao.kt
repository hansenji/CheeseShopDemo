package com.vikingsen.cheesedemo.model.database.comment

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import org.threeten.bp.LocalDateTime


@Dao
abstract class CommentDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(comments: Collection<Comment>)

    @Query("SELECT * FROM comment WHERE cheeseId = :cheeseId ORDER BY created DESC")
    abstract fun findAllByCheeseId(cheeseId: Long): LiveData<List<Comment>>

    @Query("SELECT * FROM comment WHERE id = :id")
    abstract fun findById(id: String): Comment?

    @Query("SELECT cheeseId FROM comment WHERE id = :id")
    abstract fun findCheeseId(id: String): Long?

    @Query("SELECT * FROM comment WHERE synced = 0")
    abstract fun findAllNotSynced(): List<Comment>

    @Query("UPDATE comment SET synced = 1, cached = :cached WHERE id = :id")
    abstract fun setSynced(id: String, cached: LocalDateTime)
}