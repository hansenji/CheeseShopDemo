package com.vikingsen.cheesedemo.model.webservice.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PriceDto(val id: Long, val cheeseId: Long, val price: Double)
