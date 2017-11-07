package com.vikingsen.cheesedemo.model.database.cheese

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import io.reactivex.Single
import org.threeten.bp.LocalDateTime


@Dao
interface CheeseDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cheese: Cheese)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cheeses: Collection<Cheese>)

    @Query("DELETE FROM cheese WHERE 1 = 1")
    fun deleteAll(): Int

    @Query("SELECT * FROM cheese WHERE id = :id")
    fun findById(id: Long): Cheese?

    @Query("SELECT * FROM cheese WHERE id = :id")
    @Deprecated("Move to live data")
    fun findByIdRx(id: Long): Maybe<Cheese>

    @Query("SELECT cached FROM cheese ORDER BY cached DESC LIMIT 1")
    fun findOldestCacheDate(): LocalDateTime?

    @Query("SELECT cached FROM cheese WHERE id = :id")
    fun findCacheDataById(id: Long): LocalDateTime?

    @Query("SELECT * FROM cheese ORDER BY name")
    fun findAll(): LiveData<List<Cheese>>

    @Query("SELECT * FROM cheese ORDER BY name")
    @Deprecated("Move to live data")
    fun findAllRx(): Single<List<Cheese>>
}