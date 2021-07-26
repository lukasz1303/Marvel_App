package com.example.marvel_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.marvel_app.databinding.ListComicItemBinding
import com.example.marvel_app.model.Comic


class SimpleComicAdapter(private val onClickListener: (comic: Comic) -> Unit) :
    ListAdapter<Comic, ComicViewHolder>(ComicDiffCallback()) {


    class ComicDiffCallback : DiffUtil.ItemCallback<Comic>() {
        override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(
            ListComicItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.invoke(comic)
        }
        holder.bind(getItem(position))
    }
}