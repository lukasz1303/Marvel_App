package com.example.marvel_app

import com.example.marvel_app.model.Comic

class OnClickListener(val clickListener: (comic: Comic) -> Unit) {
    fun onCLick(comic: Comic) = clickListener(comic)
}