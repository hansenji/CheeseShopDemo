package com.vikingsen.cheesedemo.model.data.cheese


import android.arch.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.database.ShopDatabase
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.cheese.CheeseDao
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseLocalDataSource
@Inject constructor(
        private val shopDatabase: ShopDatabase,
        private val cheeseDao: CheeseDao
) {
    fun getCheeses(): LiveData<List<Cheese>> {
        return cheeseDao.findAll()
    }

    fun getCheese(cheeseId: Long): LiveData<Cheese> {
        return cheeseDao.findById(cheeseId)
    }

    fun saveCheeses(cheeseDtos: List<CheeseDto>): List<Cheese> {
        val cached = LocalDateTime.now()
        val cheeses = cheeseDtos.map { (id, name, image, description) ->
            Cheese().apply {
                this.id = id
                this.name = name
                this.description = description
                this.imageUrl = image
                this.cached = cached
            }
        }
        shopDatabase.runInTransaction {
            if (cheeses.isNotEmpty()) {
                cheeseDao.deleteAll()
            }
            cheeseDao.insertAll(cheeses)
        }
        return cheeses
    }

    fun saveCheese(dto: CheeseDto): Cheese {
        val cheese = Cheese()
        cheese.id = dto.id
        cheese.name = dto.name
        cheese.description = dto.description
        cheese.imageUrl = dto.image
        cheese.cached = LocalDateTime.now()
        cheeseDao.insert(cheese)
        return cheese
    }
}
