package com.vikingsen.cheesedemo.model.repository.cheese

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.database.ShopDatabaseWrapper
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseLocalDataSource
@Inject constructor(
    private val shopDatabaseWrapper: ShopDatabaseWrapper
) {
    fun getCheeses(): LiveData<List<Cheese>> = shopDatabaseWrapper.getDatabase().cheeseDao.findAll()

    fun getCheese(cheeseId: Long) = shopDatabaseWrapper.getDatabase().cheeseDao.findById(cheeseId)

    @WorkerThread
    fun saveCheeses(cheesesDtos: List<CheeseDto>) {
        val cached = LocalDateTime.now()
        val cheeses = cheesesDtos.map {
            Cheese(
                it.id,
                it.name,
                it.description,
                it.image,
                it.sort,
                cached
            )
        }
        val database = shopDatabaseWrapper.getDatabase()
        database.runInTransaction {
            if (cheeses.isNotEmpty()) {
                database.cheeseDao.deleteAll()
            }
            database.cheeseDao.insertAll(cheeses)
        }
    }

    @WorkerThread
    fun saveCheese(cheeseDto: CheeseDto) {
        shopDatabaseWrapper.getDatabase().cheeseDao.insert(
            Cheese(
                cheeseDto.id,
                cheeseDto.name,
                cheeseDto.description,
                cheeseDto.image,
                cheeseDto.sort,
                LocalDateTime.now()
            )
        )
    }

}