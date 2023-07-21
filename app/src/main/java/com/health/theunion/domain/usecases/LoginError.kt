package com.health.theunion.domain.usecases

data class LoginError(
    val errorName: Boolean = false,
    val errorPassword: Boolean = false
)
