package com.health.theunion.domain.events

sealed class PatientReferralFormEvent {
    object NavigateToHistoryList : PatientReferralFormEvent()
    object DatePickerClick : PatientReferralFormEvent()
}
