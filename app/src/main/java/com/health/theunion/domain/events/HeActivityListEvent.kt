package com.health.theunion.domain.events

sealed class HeActivityListEvent {
    object NavigateToHeActivityForm: HeActivityListEvent()
    object ShowDateTimeDialog: HeActivityListEvent()
    data class NavigateToHistoryDetail(val id: Long): HeActivityListEvent()
}
