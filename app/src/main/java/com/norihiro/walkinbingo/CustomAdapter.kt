package com.norihiro.walkinbingo

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.norihiro.walkinbingo.databinding.BingoItemBinding

private object DiffCallback : DiffUtil.ItemCallback<BingoLabel>() {
    override fun areItemsTheSame(oldItem: BingoLabel, newItem: BingoLabel): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: BingoLabel, newItem: BingoLabel): Boolean {
        return oldItem == newItem
    }

}

class CustomAdapter(
    private val viewModel: MainViewModel
): ListAdapter<BingoLabel, CustomAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: BingoItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BingoLabel, viewModel: MainViewModel) {
            binding.run {
                bingoLabel = item
                this.viewModel = viewModel

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = BingoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

}