package com.vikingsen.controller

import com.vikingsen.model.database.price.Price
import com.vikingsen.model.database.price.PriceRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.Random
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/price")
class PriceController(val priceRepository: PriceRepository) {

    private val random = Random(42)

    @GetMapping()
    fun getPrices(): Iterable<Price> {
        return priceRepository.findAll()
    }

    @GetMapping("/{cheeseId}")
    fun getPrice(@PathVariable cheeseId: Long): Price {
        return priceRepository.findByCheeseId(cheeseId).orElseThrow { PriceNotFoundException() }
    }

    @PostMapping()
    fun updatePrices(): ResponseEntity<Any> {
        priceRepository.findAll().forEach { price ->
            price.price += genPriceDelta(price.price)
            price.price = Math.round(price.price * 100.0) / 100.0
            priceRepository.save(price)
        }
        return ResponseEntity.ok().build()
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

    private fun genPriceDelta(price: Double): Double {
        val delta = random.nextInt(10) - 5
        return when {
            Math.abs(delta) > price -> Math.abs(delta).toDouble()
            price < 10.0 -> Math.abs(delta).toDouble()
            else -> delta.toDouble()
        }
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "No such price for cheese")
class PriceNotFoundException : RuntimeException()