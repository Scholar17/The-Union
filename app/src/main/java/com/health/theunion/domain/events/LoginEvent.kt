package com.health.theunion.domain.events

sealed interface LoginEvent {
    data class ShowSnackBar(val message: String) : LoginEvent
}

