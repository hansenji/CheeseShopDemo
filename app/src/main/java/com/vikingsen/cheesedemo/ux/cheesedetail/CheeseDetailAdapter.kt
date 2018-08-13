package com.vikingsen.cheesedemo.ux.cheesedetail

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CommentItemBinding
import com.vikingsen.cheesedemo.databinding.DescriptionItemBinding
import com.vikingsen.cheesedemo.databinding.HeaderItemBinding
import com.vikingsen.cheesedemo.databinding.PriceItemBinding
import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.ui.recycler.BindingViewHolder
import java.util.ArrayList
import java.util.Locale

class CheeseDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cheese: Cheese? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var comments: List<Comment> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var price: Price? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var isLoadingPrice = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PRICE -> PriceViewHolder(parent)
            TYPE_DESCRIPTION -> DescriptionViewHolder(parent)
            TYPE_COMMENT_HEADER -> HeaderViewHolder(parent)
            TYPE_COMMENT -> CommentViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PriceViewHolder -> bindPriceViewHolder(holder)
            is DescriptionViewHolder -> bindDescriptionViewHolder(holder)
            is HeaderViewHolder -> bindHeaderViewHolder(holder)
            is CommentViewHolder -> bindCommentViewHolder(holder, position - POSITION_FIRST_COMMENT)
        }
    }

    private fun bindPriceViewHolder(holder: PriceViewHolder) {
        val price = this.price
        holder.binding.price = when {
            isLoadingPrice -> holder.binding.root.context.getString(R.string.fetching_price)
            price != null -> String.format(Locale.getDefault(), "$%.2f", price.price)
            else -> holder.binding.root.context.getString(R.string.price_unavailable)
        }
    }

    private fun bindDescriptionViewHolder(holder: DescriptionViewHolder) {
        holder.binding.cheese = cheese
    }

    private fun bindHeaderViewHolder(holder: HeaderViewHolder) {
        holder.binding.headerText = holder.binding.root.context.getString(R.string.comments)
    }

    private fun bindCommentViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment = comments.getOrNull(position)
    }

    override fun getItemCount(): Int {
        if (cheese == null) {
            return 0
        }
        var count = 3 // Description, Price, and Comment Header
        if (comments.isEmpty()) {
            count++
        }
        return count + comments.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            POSITION_PRICE -> TYPE_PRICE
            POSITION_DESCRIPTION -> TYPE_DESCRIPTION
            POSITION_COMMENT_HEADER -> TYPE_COMMENT_HEADER
            else -> TYPE_COMMENT
        }
    }

    val commentCount: Int
        get() = comments.size

    fun hasCheese() = cheese != null

    fun hasPrice() = price != null

    internal class PriceViewHolder(parent: ViewGroup) : BindingViewHolder<PriceItemBinding>(R.layout.price_item, parent)

    internal class DescriptionViewHolder(parent: ViewGroup) : BindingViewHolder<DescriptionItemBinding>(R.layout.description_item, parent)

    internal class CommentViewHolder(parent: ViewGroup) : BindingViewHolder<CommentItemBinding>(R.layout.comment_item, parent)

    internal class HeaderViewHolder(parent: ViewGroup) : BindingViewHolder<HeaderItemBinding>(R.layout.header_item, parent)

    companion object {
        private const val POSITION_PRICE = 0
        private const val POSITION_DESCRIPTION = 1
        private const val POSITION_COMMENT_HEADER = 2
        private const val POSITION_FIRST_COMMENT = 3

        private const val TYPE_PRICE = 1
        private const val TYPE_DESCRIPTION = 2
        private const val TYPE_COMMENT_HEADER = 3
        private const val TYPE_COMMENT = 4
    }
}