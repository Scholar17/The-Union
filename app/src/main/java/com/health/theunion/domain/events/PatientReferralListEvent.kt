package com.health.theunion.domain.events

sealed class PatientReferralListEvent {
    object NavigateToPatientReferralForm: PatientReferralListEvent()
    object ShowDateTimeDialog: PatientReferralListEvent()
    data class NavigateToHistoryDetail(val id: Long): PatientReferralListEvent()
}
