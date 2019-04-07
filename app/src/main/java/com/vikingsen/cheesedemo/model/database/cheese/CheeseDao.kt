package com.vikingsen.cheesedemo.model.database.cheese

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CheeseDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cheese: Cheese)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cheeses: Collection<Cheese>)

    @Query("DELETE FROM cheese WHERE 1 = 1")
    fun deleteAll(): Int

    @Query("SELECT * FROM cheese WHERE id = :id")
    fun findById(id: Long): LiveData<Cheese>

    @Query("SELECT * FROM cheese ORDER BY sort ASC, name ASC")
    fun findAll(): LiveData<List<Cheese>>
}