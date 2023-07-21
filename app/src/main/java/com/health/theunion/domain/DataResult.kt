package com.health.theunion.domain

sealed class DataResult<out T>(
    open val data: T? = null
) {
    data class Success<out T>(override val data: T) : DataResult<T>()
    data class Error(val error: String, val code: Int) : DataResult<Nothing>()
}

