package com.health.theunion.presentation

import com.health.theunion.data.he_activity.HeActivity

sealed class HeActivityFormAction {
    data class SaveDataToDb(val data: HeActivity) : HeActivityFormAction()
    data class ChangeDate(val date: String, val dateInMilli: Long) : HeActivityFormAction()

    data class ChangeAddress(val address: String) : HeActivityFormAction()
    data class ChangeVolunteer(val volunteer: String) : HeActivityFormAction()
    data class ChangeAttendeeList(val attendeeList: String) : HeActivityFormAction()
    data class ChangeMale(val male: Int) : HeActivityFormAction()
    data class ChangeFemale(val female: Int) : HeActivityFormAction()

    object DatePickerClick : HeActivityFormAction()
    object SubmitClick : HeActivityFormAction()
    object ShowErrorDialog : HeActivityFormAction()
    object ErrorDialogOk : HeActivityFormAction()
    object NavigateToHistoryList : HeActivityFormAction()
}
