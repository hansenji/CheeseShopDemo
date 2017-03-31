package com.vikingsen.model.database.price

import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface PriceRepository: CrudRepository<Price, Long> {
    fun findByCheeseId(cheeseId: Long): Optional<Price>
}