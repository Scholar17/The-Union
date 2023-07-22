package com.health.theunion.domain.events

sealed class HeActivityFormEvent {
    object NavigateToHistoryList : HeActivityFormEvent()
    object DatePickerClick : HeActivityFormEvent()
    data class ShowSnackBar(val message: String) : HeActivityFormEvent()
}
