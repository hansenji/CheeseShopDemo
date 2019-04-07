package com.vikingsen.cheesedemo.ux.cheeselist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseItemBinding
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.ui.recycler.BindingViewHolder

class CheeseListAdapter(private val viewModel: CheeseListViewModel) : ListAdapter<Cheese, CheeseListAdapter.CheeseViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        return CheeseViewHolder(parent).apply {
            binding.viewModel = viewModel
        }
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        holder.binding.cheese = getItem(position)
    }

    class CheeseViewHolder(parent: ViewGroup): BindingViewHolder<CheeseItemBinding>(R.layout.cheese_item, parent)

    companion object {
        private val DIFF_UTIL = object: DiffUtil.ItemCallback<Cheese>() {
            override fun areItemsTheSame(oldItem: Cheese, newItem: Cheese): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cheese, newItem: Cheese): Boolean {
                return oldItem.name == newItem.name && oldItem.imageUrl == newItem.imageUrl
            }

        }
    }

}