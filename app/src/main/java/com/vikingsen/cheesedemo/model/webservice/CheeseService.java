package com.vikingsen.cheesedemo.model.webservice;


import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse;
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CheeseService {

    @GET("api/v1/cheese")
    Call<List<CheeseDto>> getCheeses();

    @GET("api/v1/cheese/{cheeseId}")
    Call<CheeseDto> getCheese(@Path("cheeseId") long cheeseId);

    @GET("api/v1/price/{cheeseId}")
    Call<PriceDto> getPrice(@Path("cheeseId") long cheeseId);

    @GET("api/v1/comment/{cheeseId}")
    Call<List<CommentDto>> getComments(@Path("cheeseId") long cheeseId);

    @POST("api/v1/comment")
    Call<List<CommentResponse>> postComment(@Body List<CommentRequestDto> commentRequest);
}
