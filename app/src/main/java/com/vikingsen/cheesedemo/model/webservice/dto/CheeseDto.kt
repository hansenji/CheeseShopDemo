package com.vikingsen.cheesedemo.model.webservice.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CheeseDto(val id: Long, val name: String, val image: String, val description: String, val sort: Int)
