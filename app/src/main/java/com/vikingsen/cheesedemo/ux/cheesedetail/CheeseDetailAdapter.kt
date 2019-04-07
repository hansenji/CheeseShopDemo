package com.vikingsen.cheesedemo.ux.cheesedetail

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseCommentHeaderBinding
import com.vikingsen.cheesedemo.databinding.CheeseCommentItemBinding
import com.vikingsen.cheesedemo.databinding.CheeseDescriptionItemBinding
import com.vikingsen.cheesedemo.databinding.CheeseImageItemBinding
import com.vikingsen.cheesedemo.databinding.CheesePriceItemBinding
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.repository.price.Price
import com.vikingsen.cheesedemo.ui.recycler.BindingViewHolder
import java.util.Locale

class CheeseDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cheese: Cheese? = null
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }
    var comments: List<Comment> = listOf()
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }
    var price: Price? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var isLoadingPrice: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    fun hasPrice() = price != null
    val commentCount: Int get() = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.cheese_image_item -> ImageViewHolder(parent)
            R.layout.cheese_price_item -> PriceViewHolder(parent)
            R.layout.cheese_description_item -> DescriptionViewHolder(parent)
            R.layout.cheese_comment_header -> CommentHeaderViewHolder(parent)
            R.layout.cheese_comment_item -> CommentViewHolder(parent)
            else -> error("Invalid viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> bindImageViewHolder(holder)
            is PriceViewHolder -> bindPriceViewHolder(holder)
            is DescriptionViewHolder -> bindDescriptionViewHolder(holder)
            is CommentHeaderViewHolder -> bindCommentHeaderViewHolder(holder)
            is CommentViewHolder -> bindCommentViewHolder(holder, position - FIRST_COMMENT_POSITION)
        }
    }

    private fun bindImageViewHolder(holder: ImageViewHolder) {
        holder.binding.imageUrl = cheese?.imageUrl
    }

    private fun bindPriceViewHolder(holder: PriceViewHolder) {
        val price = this.price
        holder.binding.price = when {
            isLoadingPrice -> holder.getString(R.string.fetching_price)
            price != null -> String.format(Locale.getDefault(), "$%.2f", price.price)
            else -> holder.getString(R.string.price_unavailable)
        }
    }

    private fun bindDescriptionViewHolder(holder: DescriptionViewHolder) {
        holder.binding.description = cheese?.description
    }

    private fun bindCommentHeaderViewHolder(holder: CommentHeaderViewHolder) {
        holder.binding.header = holder.getString(R.string.comments)
    }

    private fun bindCommentViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment = comments.getOrNull(position)
    }

    override fun getItemCount(): Int {
        this.cheese ?: return 0
        return 4 + comments.size // + 1 // 4 Image, Price, Description, comment header
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.cheese_image_item
            1 -> R.layout.cheese_price_item
            2 -> R.layout.cheese_description_item
            3 -> R.layout.cheese_comment_header
            else -> R.layout.cheese_comment_item
        }
    }

    companion object {
        private const val FIRST_COMMENT_POSITION  = 4
    }

    class ImageViewHolder(parent: ViewGroup) : BindingViewHolder<CheeseImageItemBinding>(R.layout.cheese_image_item, parent)
    class DescriptionViewHolder(parent: ViewGroup) : BindingViewHolder<CheeseDescriptionItemBinding>(R.layout.cheese_description_item, parent)
    class PriceViewHolder(parent: ViewGroup) : BindingViewHolder<CheesePriceItemBinding>(R.layout.cheese_price_item, parent)
    class CommentHeaderViewHolder(parent: ViewGroup) : BindingViewHolder<CheeseCommentHeaderBinding>(R.layout.cheese_comment_header, parent)
    class CommentViewHolder(parent: ViewGroup) : BindingViewHolder<CheeseCommentItemBinding>(R.layout.cheese_comment_item, parent)

    private fun <T : ViewDataBinding> BindingViewHolder<T>.getString(@StringRes stringId: Int) = binding.root.context.getString(stringId)
}
