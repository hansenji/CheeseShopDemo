package com.vikingsen.cheesedemo.model.data.cheese


import com.vikingsen.cheesedemo.model.database.ShopDatabase
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.cheese.CheeseDao
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import io.reactivex.Maybe
import io.reactivex.Single
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseLocalDataSource
@Inject constructor(
        private val shopDatabase: ShopDatabase,
        private val cheeseDao: CheeseDao
) {

    fun getCheeses(): Single<List<Cheese>> {
        return cheeseDao.findAllRx()
    }

    fun getCheese(cheeseId: Long): Maybe<Cheese> {
        return cheeseDao.findByIdRx(cheeseId)
    }

    fun isCheeseStale(): Boolean {
        val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)

        // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
        return cheeseDao.findOldestCacheDate()?.isBefore(cacheExpiration) ?: true
    }

    fun isCheeseStale(cheeseId: Long): Boolean {
        val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
        return cheeseDao.findCacheDataById(cheeseId)?.isBefore(cacheExpiration) ?: true
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

    companion object {

        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.HOURS
    }
}
