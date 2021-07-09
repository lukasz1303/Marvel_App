package com.example.marvel_app

sealed class UIState {
    object InProgress : UIState()
    object HomeError : UIState()
    object Success : UIState()
    object InSearching : UIState()
    object SearchingError: UIState()
}