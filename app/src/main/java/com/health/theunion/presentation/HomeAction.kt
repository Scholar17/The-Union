package com.health.theunion.presentation
sealed interface HomeAction {
    object ClickPatientReferral : HomeAction
    object ClickHeActivity : HomeAction
    object ClickLogout: HomeAction
}