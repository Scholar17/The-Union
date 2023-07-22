package com.health.theunion.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.he_activity.HeActivity
import com.health.theunion.data.patient_referral.PatientReferral
import com.health.theunion.domain.events.HeActivityListEvent
import com.health.theunion.domain.events.PatientReferralListEvent
import com.health.theunion.presentation.HeActivityListAction
import com.health.theunion.presentation.PatientReferralListAction
import com.health.theunion.repository.he_activity.HeActivityHistoryRepository
import com.health.theunion.ui.udf.HeActivityListState
import com.health.theunion.ui.udf.PatientReferralListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HeActivityListViewModel @Inject constructor(
    private val repo: HeActivityHistoryRepository
) : ViewModel() {

    private val _result = mutableStateListOf<HeActivity>()
    val result: MutableList<HeActivity> get() = _result

    private val _historyListEvent = MutableSharedFlow<HeActivityListEvent>()
    val historyListEvent: SharedFlow<HeActivityListEvent> get() = _historyListEvent

    val _dateInMilli = mutableStateOf(getCurrentDateInMilli())
    private val dateInMilli: MutableState<Long> get() = _dateInMilli

    val _titleName = mutableStateOf("All Time Result")
    private val titleName: MutableState<String> get() = _titleName

    private val _historyListState = mutableStateOf(HeActivityListState())
    val historyListState: State<HeActivityListState> get() = _historyListState

    init {
        viewModelScope.launch {
            repo.deleteAllItem()
            getHistory()
        }
    }

    fun getCurrentDateInMilli(): Long {
        return GregorianCalendar.getInstance().timeInMillis
    }

    fun getHistory(){
        viewModelScope.launch {
            repo.getItems().collectLatest {
                _result.clear()
                _result.addAll(it)
                _result.sortByDescending { it.saveDateMilli }
            }
        }
    }

    private fun deleteAllItemFromDb() {
        viewModelScope.launch {
            repo.deleteAllItem()
        }
    }

    private fun deleteItemFromDb(itemId: Long) {
        viewModelScope.launch {
            repo.deleteItem(id = itemId)
        }
    }

    fun onActionHeActivityList(action: HeActivityListAction) {
        when (action) {
            is HeActivityListAction.ChangeDate -> {

            }

            HeActivityListAction.DeleteAllHistoryItem -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDeleteAllDialog = false
                    )
                }
                deleteAllItemFromDb()
            }

            is HeActivityListAction.DeleteHistoryItem -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDialog = false
                    )
                }
                deleteItemFromDb(action.id)
            }

            HeActivityListAction.DismissDeleteAllConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDeleteAllDialog = false
                    )
                }
            }

            HeActivityListAction.DismissDeleteConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDialog = false
                    )
                }
            }

            HeActivityListAction.NavigateToHeActivityForm -> {
                viewModelScope.launch {
                    _historyListEvent.emit(HeActivityListEvent.NavigateToHeActivityForm)
                }
            }

            HeActivityListAction.ShowDeleteAllConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDeleteAllDialog = true
                    )
                }
            }

            is HeActivityListAction.ShowDeleteConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDialog = true,
                        deleteItem = action.id
                    )
                }
            }

            HeActivityListAction.ShowErrorDialog -> {

            }

            is HeActivityListAction.ShowHistoryDetail -> {
                viewModelScope.launch {
                    _historyListEvent.emit(HeActivityListEvent.NavigateToHistoryDetail(id = action.id))
                }
            }
        }
    }
}