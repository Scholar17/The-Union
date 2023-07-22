package com.health.theunion.presentation

sealed class PatientReferralListAction {
    object NavigateToPatientReferralForm : PatientReferralListAction()
    data class ShowHistoryDetail(val id: Long) : PatientReferralListAction()
    data class DeleteHistoryItem(val id: Long) : PatientReferralListAction()
    data class ShowDeleteConfirmDialog(val id: Long) : PatientReferralListAction()
    object DeleteAllHistoryItem : PatientReferralListAction()
    object DismissDeleteAllConfirmDialog : PatientReferralListAction()
    object DismissDeleteConfirmDialog : PatientReferralListAction()
    object ShowErrorDialog : PatientReferralListAction()
    object ShowDeleteAllConfirmDialog : PatientReferralListAction()
    data class ChangeDate(val date: String, val dateInMilli: Long) : PatientReferralListAction()
}
