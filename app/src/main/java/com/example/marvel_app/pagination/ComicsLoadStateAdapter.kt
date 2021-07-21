package com.example.marvel_app.pagination

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ComicsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ComicsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ComicsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ComicsLoadStateViewHolder {
        return ComicsLoadStateViewHolder.create(parent, retry)
    }
}
