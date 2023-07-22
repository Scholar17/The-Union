package com.health.theunion.presentation

sealed class HeActivityListAction {
    object NavigateToHeActivityForm : HeActivityListAction()
    data class ShowHistoryDetail(val id: Long) : HeActivityListAction()
    data class DeleteHistoryItem(val id: Long) : HeActivityListAction()
    data class ShowDeleteConfirmDialog(val id: Long) : HeActivityListAction()
    object DeleteAllHistoryItem : HeActivityListAction()
    object DismissDeleteAllConfirmDialog : HeActivityListAction()
    object DismissDeleteConfirmDialog : HeActivityListAction()
    object ShowErrorDialog : HeActivityListAction()
    object ShowDeleteAllConfirmDialog : HeActivityListAction()
    data class ChangeDate(val date: String, val dateInMilli: Long) : HeActivityListAction()
}
