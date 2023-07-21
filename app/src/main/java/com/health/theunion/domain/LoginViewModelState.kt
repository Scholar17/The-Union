package com.health.theunion.domain

import com.health.theunion.domain.usecases.LoginError
import com.health.theunion.domain.usecases.LoginForm

data class LoginViewModelState(
    val loginLoading: Boolean = false,
    val loginError: LoginError = LoginError(),
    val loginForm: LoginForm = LoginForm(),
) {
    fun loginLoading() = loginLoading
    fun loginError() = loginError
    fun loginForm() = loginForm
}