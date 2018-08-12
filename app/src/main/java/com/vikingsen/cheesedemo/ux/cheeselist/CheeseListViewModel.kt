package com.vikingsen.cheesedemo.ux.cheeselist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.vikingsen.cheesedemo.model.AbsentLiveData
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.SingleLiveEvent
import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import javax.inject.Inject

class CheeseListViewModel
@Inject constructor(
        application: Application,
        private val cheeseRepository: CheeseRepository
) : AndroidViewModel(application) {
    private val forceLoad = MutableLiveData<Boolean>()

    val cheeses: LiveData<Resource<List<Cheese>>>
    val cheeseSelected = SingleLiveEvent<Cheese>()

    init {
        cheeses = AbsentLiveData.nullSwitchMap(forceLoad) {
            cheeseRepository.getCheeses(it)
        }
        forceLoad.value = false
    }

    fun forceLoad() {
        forceLoad.value = true
    }

    fun onCheeseClicked(cheese: Cheese) {
        cheeseSelected.value = cheese
    }
}