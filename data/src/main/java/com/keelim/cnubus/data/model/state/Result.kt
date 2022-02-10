package com.keelim.cnubus.data.model.state

sealed class Result<out T> {
    data class Success<out R>(val data: R) : Result<R>()
    data class Error(val exception: Exception): Result<Nothing>()
}