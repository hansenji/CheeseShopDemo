package com.vikingsen.cheesedemo.ux.cheesedetail


import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devbrackets.android.recyclerext.adapter.RecyclerHeaderAdapter
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.room.cheese.Cheese
import com.vikingsen.cheesedemo.model.room.comment.Comment
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_description.view.*
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_price.view.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.ArrayList
import java.util.Locale

class CheeseDetailAdapter : RecyclerHeaderAdapter<CheeseDetailAdapter.HeaderViewHolder, RecyclerView.ViewHolder>() {


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
        set(loadingPrice) {
            field = loadingPrice
            notifyDataSetChanged()
        }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(parent)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_PRICE -> return PriceViewHolder(parent)
            TYPE_DESCRIPTION -> return DescriptionViewHolder(parent)
            TYPE_COMMENT -> return CommentViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid type " + viewType)
        }
    }

    override fun onBindHeaderViewHolder(holder: HeaderViewHolder, firstChildPosition: Int) {
        holder.headerTextView.setText(R.string.comments)
    }

    override fun onBindChildViewHolder(holder: RecyclerView.ViewHolder, childPosition: Int) {
        if (holder is PriceViewHolder) {
            bindPriceViewHolder(holder)
        } else if (holder is DescriptionViewHolder) {
            bindDescriptionViewHolder(holder)
        } else if (holder is CommentViewHolder) {
            bindCommentViewHolder(holder, childPosition - POSITION_FIRST_COMMENT)
        }
    }

    override fun getChildCount(): Int {
        if (cheese == null) {
            return 0
        }
        var count = 2
        if (comments.isEmpty()) {
            count++
        }
        return count + comments.size
    }

    override fun getHeaderId(childPosition: Int): Long {
        if (childPosition >= POSITION_FIRST_COMMENT) {
            return COMMENT_HEADER_ID
        }
        return RecyclerView.NO_ID
    }

    override fun getChildViewType(childPosition: Int): Int {
        when (childPosition) {
            POSITION_PRICE -> return TYPE_PRICE
            POSITION_DESCRIPTION -> return TYPE_DESCRIPTION
            else -> return TYPE_COMMENT
        }
    }

    val commentCount: Int
        get() = comments.size

    fun hasCheese(): Boolean {
        return cheese != null
    }

    fun hasPrice(): Boolean {
        return price != null
    }

    private fun bindPriceViewHolder(holder: PriceViewHolder) {
        val price = this.price
        if (isLoadingPrice) {
            holder.priceTextView.setText(R.string.fetching_price)
        } else if (price != null) {
            holder.priceTextView.text = String.format(Locale.getDefault(), "$%.2f", price.price)
        } else {
            holder.priceTextView.setText(R.string.price_unavailable)
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
    private fun getTextColor(syncedWithServer: Boolean): Int {
        return if (syncedWithServer) 0xde000000.toInt() else 0x8a000000.toInt()
    }

    private fun getDateText(comment: Comment): String {
        val date = comment.created
        return date.format(FORMATTER)
    }

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

    class HeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)) {
        val headerTextView: TextView = itemView.headerTextView
    }

    companion object {

        private val COMMENT_HEADER_ID: Long = 1

        private val POSITION_PRICE = 0
        private val POSITION_DESCRIPTION = 1
        private val POSITION_FIRST_COMMENT = 2

        private val TYPE_PRICE = 1
        private val TYPE_DESCRIPTION = 2
        private val TYPE_COMMENT = 3

        private val FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    }
}
