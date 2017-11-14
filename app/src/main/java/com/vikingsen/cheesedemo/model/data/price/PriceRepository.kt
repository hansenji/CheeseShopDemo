package com.vikingsen.cheesedemo.model.data.price

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject
internal constructor(private val remoteDataSource: PriceRemoteDataSource) {

    fun getPrice(cheeseId: Long, @Suppress("UNUSED_PARAMETER") forceRefresh: Boolean): LiveData<Resource<Price>> {
        return Transformations.map(remoteDataSource.getPrice(cheeseId)) {
            @Suppress("RemoveExplicitTypeArguments") //Technically I only need one <Price>
            when {
                it == null -> Resource.Loading<Price>()
                it.successful -> Resource.Success(mapToPrice(it.data))
                else -> Resource.Error<Price>()
            }
        }
    }

    private fun mapToPrice(data: PriceDto?): Price? {
        return data?.let { (_, cheeseId, price) ->
            Price(cheeseId, price)
        }
    }
}
