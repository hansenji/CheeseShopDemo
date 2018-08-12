package com.vikingsen.cheesedemo.ux.cheeselist

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseItemBinding
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.ui.recycler.BindingViewHolder
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListAdapter.CheeseViewHolder

internal class CheeseListAdapter(private val viewModel: CheeseListViewModel) : ListAdapter<Cheese, CheeseViewHolder>(CheeseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        val cheeseViewHolder = CheeseViewHolder(parent)
        cheeseViewHolder.binding.viewModel = viewModel
        return cheeseViewHolder
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        holder.binding.cheese = getItem(position)
    }

    internal class CheeseViewHolder(parent: ViewGroup) : BindingViewHolder<CheeseItemBinding>(R.layout.cheese_item, parent)

    private class CheeseDiffUtil: DiffUtil.ItemCallback<Cheese>() {
        override fun areItemsTheSame(oldItem: Cheese?, newItem: Cheese?): Boolean {
            return oldItem?.id == newItem?.id
        }

        override fun areContentsTheSame(oldItem: Cheese?, newItem: Cheese?): Boolean {
            return oldItem?.name == newItem?.name && oldItem?.imageUrl == newItem?.imageUrl
        }

    }
}
