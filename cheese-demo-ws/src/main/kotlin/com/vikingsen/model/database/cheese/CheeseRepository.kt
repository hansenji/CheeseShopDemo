package com.vikingsen.model.database.cheese

import org.springframework.data.repository.CrudRepository

interface CheeseRepository: CrudRepository<Cheese, Long>