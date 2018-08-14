package com.vikingsen.cheesedemo.model.database.cheese

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


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