package com.example.marvel_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel_app.databinding.ListComicItemBinding
import com.example.marvel_app.model.Comic

class ComicsAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Comic, ComicsAdapter.ComicViewHolder>(ComicDiffCallback()) {

    class ComicViewHolder(private val binding: ListComicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comic: Comic) {
            binding.viewHolder = comic
            binding.executePendingBindings()
        }
    }

    class ComicDiffCallback : DiffUtil.ItemCallback<Comic>() {
        override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(ListComicItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onCLick(comic)
        }
        holder.bind(getItem(position))
    }
}

