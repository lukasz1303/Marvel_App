package com.example.marvel_app

import androidx.recyclerview.widget.RecyclerView
import com.example.marvel_app.databinding.ListComicItemBinding
import com.example.marvel_app.model.Comic

class ComicViewHolder(private val binding: ListComicItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(comic: Comic) {
        binding.viewHolder = comic
        binding.executePendingBindings()
    }
}