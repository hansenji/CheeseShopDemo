package com.vikingsen

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.thedeanda.lorem.LoremIpsum
import com.vikingsen.model.database.cheese.Cheese
import com.vikingsen.model.database.cheese.CheeseRepository
import com.vikingsen.model.database.comment.Comment
import com.vikingsen.model.database.comment.CommentRepository
import com.vikingsen.model.database.price.Price
import com.vikingsen.model.database.price.PriceRepository
import com.vikingsen.model.init.CheesesDto
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Random
import java.util.UUID


@SpringBootApplication
class CheeseDemoWsApplication {

    private val random = Random(42)
    private val bytes = ByteArray(42)
    private val lorem = LoremIpsum(42)
    private val name: String by lazy { lorem.firstName }

    @Bean
    fun init(cheeseRepository: CheeseRepository,
             priceRepository: PriceRepository,
             commentRepository: CommentRepository
    ): CommandLineRunner {

        return CommandLineRunner {
            val cheeses = ObjectMapper().readValue(javaClass.classLoader.getResource("cheese.json"), CheesesDto::class.java).cheese
            cheeses.forEach { (name, image, order) ->
                val cheese = cheeseRepository.save(Cheese(name = name, image = "/images/$image", description = lorem.getParagraphs(1, 1), sort = order))
                priceRepository.save(Price(cheeseId = cheese.id, price = getPrice()))
                for (i in 1..getNumComments()) {
                    commentRepository.save(genComment(cheese, i))
                }
            }
        }
    }

    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.serializationInclusion(JsonInclude.Include.NON_NULL)
        builder.serializers(LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME))
        return builder
    }

    private fun genComment(cheese: Cheese, index: Int): Comment {
        val created = getDate(index)
        return Comment(guid = nextGuid(),
                cheeseId = cheese.id,
                user = getUser(index),
                comment = lorem.getParagraphs(1, 2),
                created = created)
    }

    private fun getDate(index: Int): LocalDateTime {
        val i = index + 1
        return LocalDateTime.now()
                .minusWeeks(random.nextInt(i).toLong())
                .minusDays(random.nextInt(i * 2).toLong())
                .minusHours(random.nextInt(i * 4).toLong())
                .minusMinutes(random.nextInt(i * 8).toLong())
    }

    private fun getUser(index: Int): String {
        return when (index) {
            2 -> name
            else -> lorem.firstName
        }
    }

    private fun getNumComments(): Int {
        return Math.max(0, random.nextInt(5) - 1)
    }

    private fun getPrice(): Double {
        val dollars = random.nextInt(25)
        val cents = random.nextInt(100)
        return "$dollars.$cents".toDouble()
    }

    private fun nextGuid(): String {
        random.nextBytes(bytes)
        return UUID.nameUUIDFromBytes(bytes).toString()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CheeseDemoWsApplication::class.java, *args)
}
