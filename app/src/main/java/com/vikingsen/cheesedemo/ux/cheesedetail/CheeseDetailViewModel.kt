package com.vikingsen.cheesedemo.ux.cheesedetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vikingsen.cheesedemo.model.AbsentLiveData
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository
import com.vikingsen.cheesedemo.model.data.comment.CommentRepository
import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.data.price.PriceRepository
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import javax.inject.Inject

class CheeseDetailViewModel
@Inject constructor(private val cheeseRepository: CheeseRepository,
                    private val commentRepository: CommentRepository,
                    private val priceRepository: PriceRepository) : ViewModel() {

    private val cheeseParams = MutableLiveData<CheeseParam>()

    val cheese: LiveData<Resource<Cheese>>
    val comments: LiveData<Resource<List<Comment>>>
    val price: LiveData<Resource<Price>>

    init {
        cheese = AbsentLiveData.nullSwitchMap(cheeseParams) { (cheeseId, force) ->
            cheeseRepository.getCheese(cheeseId, force)
        }
        comments = AbsentLiveData.nullSwitchMap(cheeseParams) { (cheeseId, force) ->
            commentRepository.getComments(cheeseId, force)
        }
        price = AbsentLiveData.nullSwitchMap(cheeseParams) { (cheeseId, force) ->
            priceRepository.getPrice(cheeseId, force)
        }
    }

    fun addNewComment(user: String, comment: String) {
        cheeseParams.value?.let {
            commentRepository.addComment(it.cheeseId, user, comment)
        }
    }

    fun setCheeseId(cheeseId: Long) {
        if (this.cheeseParams.value?.cheeseId != cheeseId) {
            this.cheeseParams.value = CheeseParam(cheeseId, false)
        }
    }

    fun reload() {
        cheeseParams.value?.let {
            cheeseParams.value = it.copy(force = true)
        }
    }

    private data class CheeseParam(val cheeseId: Long, val force: Boolean)
}