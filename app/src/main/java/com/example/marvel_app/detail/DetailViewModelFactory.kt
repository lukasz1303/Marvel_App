package com.example.marvel_app.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marvel_app.model.Comic
import java.lang.IllegalArgumentException

class DetailViewModelFactory(
    private val comic: Comic,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(comic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}