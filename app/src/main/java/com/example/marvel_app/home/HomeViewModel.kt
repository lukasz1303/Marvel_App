package com.example.marvel_app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_app.model.Comic

class HomeViewModel: ViewModel() {

    private val _comics = MutableLiveData<List<Comic>>()
    val comics : LiveData<List<Comic>>
    get() = _comics

    init {
        _comics.value = listOf(Comic("Star Wars 1", null, "Some description 1", listOf("Author 1")),
            Comic("Star Wars 2", null, "Some description 2", listOf("Author 2")),
            Comic("Star Wars 3", null, "Some description 3", listOf("Author 3")),
            Comic("Star Wars 4", null, "Some description 4", listOf("Author 4", "Author 5")))
    }
}