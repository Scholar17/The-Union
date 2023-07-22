package com.health.theunion.ui.udf

data class PatientReferralListState(
    val shouldShowDialog: Boolean = false,
    val shouldShowErrorDialog: Boolean = false,
    val shouldShowStartDateErrorDialog: Boolean = false,
    val shouldShowEndDateErrorDialog: Boolean = false,
    val shouldShowDatePickerDialog: Boolean = false,
    val deleteItem: Long = -1L,
    val endDateErrorText: String = "",
    val shouldShowDeleteAllDialog: Boolean = false
)
