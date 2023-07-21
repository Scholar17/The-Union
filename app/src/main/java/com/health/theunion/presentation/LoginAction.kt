package com.health.theunion.presentation

sealed interface LoginAction {
    object ClickLogin : LoginAction
    data class ChangeUserName(val name: String) : LoginAction
    data class ChangePassword(val password: String) : LoginAction
}