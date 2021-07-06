package com.example.marvel_app

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel_app.model.Comic

@BindingAdapter("comics")
fun bindRecyclerView(recyclerView: RecyclerView, comics: List<Comic>?){
    val adapter = recyclerView.adapter as ComicsAdapter
    adapter.submitList(comics)
}

@BindingAdapter("authors")
fun formatAuthors(textView: TextView, authors: List<String>){
    var formattedText = "written by "
    for (author in authors){
        formattedText += "$author, "
    }
    formattedText = formattedText.subSequence(0,formattedText.length-2) as String
    textView.text = formattedText
}