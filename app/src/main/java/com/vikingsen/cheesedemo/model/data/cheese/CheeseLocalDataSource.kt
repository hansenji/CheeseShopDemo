package com.vikingsen.cheesedemo.model.data.cheese


import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.cheese.CheeseManager
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import io.reactivex.Maybe
import io.reactivex.Single
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseLocalDataSource @Inject
constructor(private val cheeseManager: CheeseManager) {

    fun getCheeses(): Single<List<Cheese>> {
        return cheeseManager.findAllCheesesRx()
    }

    fun getCheese(cheeseId: Long?): Maybe<Cheese> {
        return cheeseManager.findByCheeseIdRx(cheeseId!!)
    }

    fun isCheeseStale(): Boolean {
        val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)

        // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
        return cheeseManager.findOldestCacheDate().isBefore(cacheExpiration)
    }

    fun isCheeseStale(cheeseId: Long): Boolean {
        val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
        return cheeseManager.findCacheDate(cheeseId).isBefore(cacheExpiration)
    }

    fun saveCheeses(cheeseDtos: List<CheeseDto>): List<Cheese> {
        val cached = LocalDateTime.now()
        val cheeses = ArrayList<Cheese>(cheeseDtos.size)
        cheeseManager.beginTransaction()
        var commit = false
        try {
            if (!cheeses.isEmpty()) {
                cheeseManager.deleteAll()
            }
            for ((id, name, image, description) in cheeseDtos) {
                val cheese = Cheese()
                cheese.id = id
                cheese.name = name
                cheese.description = description
                cheese.imageUrl = image
                cheese.cached = cached
                cheeseManager.save(cheese)
                cheeses.add(cheese)
            }
            commit = true
        } finally {
            cheeseManager.endTransaction(commit)
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
        cheeseManager.save(cheese)
        return cheese
    }

    companion object {

        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.HOURS
    }
}
