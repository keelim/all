package com.keelim.mygrade.ui.center.main

sealed class MainState {
    object UnInitialized : MainState()
    object Loading : MainState()
    data class Success(
        val flag: Boolean,
        val value: Int
    ) : MainState()
    data class Error(val message: String) : MainState()
}
