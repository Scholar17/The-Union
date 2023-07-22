package com.health.theunion.presentation

import com.health.theunion.data.patient_referral.PatientReferral

sealed class PatientReferralFormAction {
    data class SaveDataToDb(val data: PatientReferral) : PatientReferralFormAction()
    data class chooseRadio(val isMorning: Boolean) : PatientReferralFormAction()
    data class ChangeDate(val date: String, val dateInMilli: Long) : PatientReferralFormAction()
    object DatePickerClick : PatientReferralFormAction()
    object ShowErrorDialog : PatientReferralFormAction()
    object ErrorDialogOk : PatientReferralFormAction()
    object NavigateToHistoryList : PatientReferralFormAction()
}