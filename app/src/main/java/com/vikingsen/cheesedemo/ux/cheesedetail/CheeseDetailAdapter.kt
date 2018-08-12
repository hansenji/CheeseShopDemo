package com.vikingsen.cheesedemo.ux.cheesedetail

import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_description.view.*
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_price.view.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
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
            is HeaderViewHolder -> holder.headerTextView.setText(R.string.comments)
            is CommentViewHolder -> bindCommentViewHolder(holder, position - POSITION_FIRST_COMMENT)
        }
    }

    private fun bindPriceViewHolder(holder: PriceViewHolder) {
        val price = this.price
        when {
            isLoadingPrice -> holder.priceTextView.setText(R.string.fetching_price)
            price != null -> holder.priceTextView.text = String.format(Locale.getDefault(), "$%.2f", price.price)
            else -> holder.priceTextView.setText(R.string.price_unavailable)
        }
    }

    private fun bindDescriptionViewHolder(holder: DescriptionViewHolder) {
        cheese?.let {
            holder.descriptionTextView.text = it.description
        }
    }

    private fun bindCommentViewHolder(holder: CommentViewHolder, position: Int) {
        if (position < 0 || position >= comments.size) {
            holder.itemView.visibility = View.INVISIBLE
            return
        }
        val comment = comments[position]
        holder.itemView.visibility = View.VISIBLE
        holder.commentTextView.text = comment.comment
        holder.commentTextView.setTextColor(getTextColor(comment.synced))
        holder.userTextView.text = comment.user
        holder.dateTextView.text = getDateText(comment)
    }

    @ColorInt
    private fun getTextColor(syncedWithServer: Boolean): Int = if (syncedWithServer) 0xde000000.toInt() else 0x8a000000.toInt()

    private fun getDateText(comment: Comment): String {
        val date = comment.created
        return date.format(FORMATTER)
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

    internal class PriceViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_price, parent, false)) {
        val priceTextView: TextView = itemView.priceTextView
    }

    internal class DescriptionViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_description, parent, false)) {
        val descriptionTextView: TextView = itemView.descriptionTextView
    }

    internal class CommentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)) {
        val commentTextView: TextView = itemView.commentTextView
        val userTextView: TextView = itemView.userTextView
        val dateTextView: TextView = itemView.dateTextView
    }

    internal class HeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)) {
        val headerTextView: TextView = itemView.headerTextView
    }

    companion object {

        private const val POSITION_PRICE = 0
        private const val POSITION_DESCRIPTION = 1
        private const val POSITION_COMMENT_HEADER = 2
        private const val POSITION_FIRST_COMMENT = 3

        private const val TYPE_PRICE = 1
        private const val TYPE_DESCRIPTION = 2
        private const val TYPE_COMMENT_HEADER = 3
        private const val TYPE_COMMENT = 4

        private val FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    }
}