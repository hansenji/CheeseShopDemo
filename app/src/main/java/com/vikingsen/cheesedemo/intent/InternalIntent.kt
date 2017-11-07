package com.vikingsen.cheesedemo.intent


import android.content.Context
import android.content.Intent

import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailActivity

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternalIntent
@Inject constructor() {

    fun getCheeseDetailIntent(context: Context, cheeseId: Long, cheeseName: String): Intent {
        val intent = Intent(context, CheeseDetailActivity::class.java)
        intent.putExtra(CheeseDetailActivity.EXTRA_CHEESE_ID, cheeseId)
        intent.putExtra(CheeseDetailActivity.EXTRA_CHEESE_NAME, cheeseName)
        return intent
    }
}
