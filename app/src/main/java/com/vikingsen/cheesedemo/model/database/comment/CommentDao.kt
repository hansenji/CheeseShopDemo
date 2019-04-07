package com.vikingsen.cheesedemo.model.database.comment

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.threeten.bp.LocalDateTime

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(comments: Collection<Comment>)

    @Query("SELECT * FROM comment WHERE cheeseId = :cheeseId ORDER BY created DESC")
    fun findAllByCheeseId(cheeseId: Long): LiveData<List<Comment>>

    @Query("SELECT * FROM comment WHERE id = :id")
    fun findById(id: String): Comment?

    @Query("SELECT cheeseId FROM comment WHERE id = :id")
    fun findCheeseId(id: String): Long?

    @Query("SELECT * FROM comment WHERE synced = 0")
    fun findAllNotSynced(): List<Comment>

    @Query("UPDATE comment SET synced = 1, cached = :cached WHERE id = :id")
    fun setSynced(id: String, cached: LocalDateTime)
}