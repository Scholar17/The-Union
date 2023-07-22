package com.health.theunion.presentation

import com.health.theunion.data.patient_referral.PatientReferral

sealed class PatientReferralFormAction {
    data class SaveDataToDb(val data: PatientReferral) : PatientReferralFormAction()
    data class chooseRadio(val sex: Int) : PatientReferralFormAction()
    data class ChangeDate(val date: String, val dateInMilli: Long) : PatientReferralFormAction()

    data class ChangeUserName(val name: String) : PatientReferralFormAction()
    data class ChangeAddress(val address: String) : PatientReferralFormAction()
    data class ChangeAge(val age: Int) : PatientReferralFormAction()
    data class ChangeTownship(val township: String) : PatientReferralFormAction()
    data class ChangeReferFrom(val referFrom: String) : PatientReferralFormAction()
    data class ChangeReferTo(val referTo: String) : PatientReferralFormAction()
    data class ChangeSignAndSymptom(val signAndSymptom: String) : PatientReferralFormAction()
    object DatePickerClick : PatientReferralFormAction()
    object SubmitClick : PatientReferralFormAction()
    object ShowErrorDialog : PatientReferralFormAction()
    object ErrorDialogOk : PatientReferralFormAction()
    object NavigateToHistoryList : PatientReferralFormAction()
}