package com.health.theunion.domain.events

sealed interface HomeEvent {
    object NavigateToPatientReferral : HomeEvent
    object NavigateToHeActivity : HomeEvent
    object NavigateToLogin : HomeEvent
}
