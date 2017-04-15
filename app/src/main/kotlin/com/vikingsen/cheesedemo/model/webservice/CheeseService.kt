package com.vikingsen.cheesedemo.model.webservice


import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CheeseService {

    @GET("api/v1/cheese")
    fun getCheeses(): Call<List<CheeseDto>>

    @GET("api/v1/cheese/{cheeseId}")
    fun getCheese(@Path("cheeseId") cheeseId: Long): Call<CheeseDto>

    @GET("api/v1/price/{cheeseId}")
    fun getPrice(@Path("cheeseId") cheeseId: Long): Call<PriceDto>

    @GET("api/v1/comment/{cheeseId}")
    fun getComments(@Path("cheeseId") cheeseId: Long): Call<List<CommentDto>>

    @POST("api/v1/comment")
    fun postComment(@Body commentRequest: List<CommentRequestDto>): Call<List<CommentResponse>>
}
