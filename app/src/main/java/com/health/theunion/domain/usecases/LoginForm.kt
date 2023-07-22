package com.health.theunion.domain.usecases

data class LoginForm(
    val userName: String = "Admin",
    val password: String = "Admin123"
)
