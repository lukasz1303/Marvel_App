package com.example.marvel_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.marvel_app.databinding.ListComicItemBinding
import com.example.marvel_app.model.Comic

class ComicsAdapter(private val onClickListener: (comic: Comic) -> Unit) :
    PagingDataAdapter<Comic, ComicViewHolder>(ComicDiffCallback()) {

    class ComicDiffCallback : DiffUtil.ItemCallback<Comic>() {
        override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem.id == newItem.id
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
            if (comic != null) {
                onClickListener.invoke(comic)
            }
        }
        getItem(position)?.let { holder.bind(it) }
    }
}

