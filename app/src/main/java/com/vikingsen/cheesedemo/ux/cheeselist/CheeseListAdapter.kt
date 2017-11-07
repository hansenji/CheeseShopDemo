package com.vikingsen.cheesedemo.ux.cheeselist


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.devbrackets.android.recyclerext.adapter.viewholder.ClickableViewHolder
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import kotlinx.android.synthetic.main.item_cheese.view.*
import java.util.ArrayList

internal class CheeseListAdapter(private val glideRequestManager: RequestManager) : RecyclerView.Adapter<CheeseListAdapter.CheeseViewHolder>() {

    var onClickListener: (Cheese) -> Unit = {}
    var cheeses: List<Cheese> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        val cheeseViewHolder = CheeseViewHolder(parent)
        cheeseViewHolder.setOnClickListener { viewHolder ->
            onClickListener(cheeses[viewHolder.adapterPosition])
        }
        return cheeseViewHolder
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        val cheese = cheeses[position]
        holder.icTextView.text = cheese.name
        holder.icImageView.contentDescription = cheese.name

        glideRequestManager.load(BuildConfig.IMAGE_BASE_URL + cheese.imageUrl)
                .apply(RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_broken_image_white_48dp)
                )
                .into(holder.icImageView)
    }

    override fun getItemCount(): Int {
        return cheeses.size
    }

    internal class CheeseViewHolder(parent: ViewGroup) : ClickableViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cheese, parent, false)) {
        var icImageView: ImageView = itemView.icImageView
        var icTextView: TextView = itemView.icTextView
    }
}
