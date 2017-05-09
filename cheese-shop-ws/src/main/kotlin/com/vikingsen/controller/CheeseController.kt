package com.vikingsen.controller

import com.vikingsen.model.database.cheese.Cheese
import com.vikingsen.model.database.cheese.CheeseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/cheese")
class CheeseController(val cheeseRepository: CheeseRepository) {
    @GetMapping()
    fun findAll(): List<Cheese> {
        return cheeseRepository.findAllByOrderByNameAsc()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Cheese {
//        return cheeseRepository.findOne(id).orElse(null) ?: throw CheeseNotFoundException()
        return cheeseRepository.findOne(id) ?: throw CheeseNotFoundException()
    }

    @ExceptionHandler(Throwable::class)
    @ResponseBody
    fun handleException(req: HttpServletRequest, throwable: Throwable): ResponseEntity<Any> {
        return when (throwable) {
            is CheeseNotFoundException -> ResponseEntity.notFound().build()
            is PriceNotFoundException -> ResponseEntity.notFound().build()
            else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "No such Cheese")
class CheeseNotFoundException: RuntimeException()
