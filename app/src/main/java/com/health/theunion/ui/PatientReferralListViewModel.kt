package com.health.theunion.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.patient_referral.PatientReferral
import com.health.theunion.domain.events.PatientReferralListEvent
import com.health.theunion.presentation.PatientReferralListAction
import com.health.theunion.repository.patient_referral.PatientReferralHistoryRepository
import com.health.theunion.ui.udf.PatientReferralListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class PatientReferralListViewModel @Inject constructor(private val repo: PatientReferralHistoryRepository) : ViewModel() {

    private val _result = mutableStateListOf<PatientReferral>()
    val result: MutableList<PatientReferral> get() = _result

    private val _historyListEvent = MutableSharedFlow<PatientReferralListEvent>()
    val historyListEvent: SharedFlow<PatientReferralListEvent> get() = _historyListEvent

    val _dateInMilli = mutableStateOf(getCurrentDateInMilli())
    private val dateInMilli: MutableState<Long> get() = _dateInMilli

    val _titleName = mutableStateOf("All Time Result")
    private val titleName: MutableState<String> get() = _titleName

    private val _historyListState = mutableStateOf(PatientReferralListState())
    val historyListState: State<PatientReferralListState> get() = _historyListState

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

    fun onActionPatientReferralList(action: PatientReferralListAction) {
        when (action) {
            PatientReferralListAction.NavigateToPatientReferralForm -> {
                viewModelScope.launch {
                    _historyListEvent.emit(PatientReferralListEvent.NavigateToPatientReferralForm)
                }
            }
            PatientReferralListAction.DeleteAllHistoryItem -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDeleteAllDialog = false
                    )
                }
                deleteAllItemFromDb()
            }
            is PatientReferralListAction.DeleteHistoryItem -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDialog = false
                    )
                }
                deleteItemFromDb(action.id)
            }
            is PatientReferralListAction.ShowHistoryDetail -> {
                viewModelScope.launch {
                    _historyListEvent.emit(PatientReferralListEvent.NavigateToHistoryDetail(id = action.id))
                }
            }
            is PatientReferralListAction.ShowDeleteConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDialog = true,
                        deleteItem = action.id
                    )
                }
            }

            is PatientReferralListAction.ChangeDate -> TODO()
            PatientReferralListAction.ShowErrorDialog -> TODO()
            PatientReferralListAction.DismissDeleteAllConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDeleteAllDialog = false
                    )
                }
            }

            PatientReferralListAction.ShowDeleteAllConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDeleteAllDialog = true
                    )
                }
            }

            PatientReferralListAction.DismissDeleteConfirmDialog -> {
                viewModelScope.launch {
                    _historyListState.value = historyListState.value.copy(
                        shouldShowDialog = false
                    )
                }
            }
        }
    }
}