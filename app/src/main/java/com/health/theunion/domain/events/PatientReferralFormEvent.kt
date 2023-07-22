package com.health.theunion.domain.events

sealed class PatientReferralFormEvent {
    object NavigateToHistoryList : PatientReferralFormEvent()
    object DatePickerClick : PatientReferralFormEvent()
    data class ShowSnackBar(val message: String) : PatientReferralFormEvent()
}
