package com.joaoreis.catbreeds

sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()
    class Error<T>: Result<T>()
}