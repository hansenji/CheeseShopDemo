package com.vikingsen.cheesedemo.ux.cheesedetail


import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment

interface CheeseDetailContract {
    interface View {
        //        void showLoading(boolean loading);
        fun showCheese(cheese: Cheese)

        fun showCheeseError()
        fun showMissingCheese()

        fun showComments(comments: List<Comment>)
        fun showCommentError()

        fun showPriceLoading(loading: Boolean)
        fun showPrice(price: Price)
        fun showPriceError(networkDisconnected: Boolean)
    }
}
