package com.health.theunion.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.he_activity.HeActivity
import com.health.theunion.domain.HeActivityFormState
import com.health.theunion.domain.events.HeActivityFormEvent
import com.health.theunion.domain.usecases.HeActivityFormUseCase
import com.health.theunion.presentation.HeActivityFormAction
import com.health.theunion.repository.he_activity.HeActivityHistoryRepository
import com.health.theunion.ui.udf.HeActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HeActivityFormViewModel @Inject constructor(
    private val repo: HeActivityHistoryRepository,
    private val useCase: HeActivityFormUseCase,
) : ViewModel() {
    val _date = mutableStateOf(getCurrentDate())
    private val date: MutableState<String> get() = _date

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val calendar = GregorianCalendar.getInstance()
        val fullDate = SimpleDateFormat("dd-MM-yyyy")
        Timber.tag("get month").d(fullDate.format(calendar.time))
        return fullDate.format(calendar.time).toString()
    }

    fun getCurrentDateInMilli(): Long {
        return GregorianCalendar.getInstance().timeInMillis
    }

    private val _heActivityFormEvent = MutableSharedFlow<HeActivityFormEvent>()
    val heActivityFormEvent: SharedFlow<HeActivityFormEvent> get() = _heActivityFormEvent

    val _dateInMilli = mutableStateOf(getCurrentDateInMilli())
    private val dateInMilli: MutableState<Long> get() = _dateInMilli

    private val vmState = MutableStateFlow(HeActivityFormState())

    val _formState = mutableStateOf(HeActivityState())
    private val formState: State<HeActivityState> get() = _formState


    val heActivityLoading = vmState
        .map(HeActivityFormState::heActivityLoading)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.heActivityLoading()
        )

    val heActivityError = vmState
        .map(HeActivityFormState::heActivityError)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.heActivityError()
        )

    val patientReferralForm = vmState
        .map(HeActivityFormState::heActivityForm)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.heActivityForm()
        )

    init {
        viewModelScope.launch {
            vmState.update { state ->
                state.copy(
                    heActivityForm = state.heActivityForm.copy(
                        date = _date.value
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    heActivityForm = state.heActivityForm.copy(
                        saveDateMilli = _dateInMilli.value
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    heActivityForm = state.heActivityForm.copy(
                        volunteer = "Vol1"
                    )
                )
            }
        }
    }

    private fun validateForm() {
        useCase.validator(
            heActivityForm = vmState.value.heActivityForm()
        ).apply {
            vmState.update { state ->
                state.copy(
                    heActivityError = this
                )
            }
        }.also {
            if (!it.errorAddress && !it.errorAttendee && !it.errorMale && !it.errorFemale) {
                vmState.update { state ->
                    state.copy(
                        heActivityLoading = true
                    )
                }
                viewModelScope.launch {
                    repo.addItem(
                        heActivity = HeActivity(
                            address = vmState.value.heActivityForm.address,
                            volunteer = vmState.value.heActivityForm.volunteer,
                            heAttendeesList = vmState.value.heActivityForm.attendeesList,
                            male = vmState.value.heActivityForm.male,
                            female = vmState.value.heActivityForm.female,
                            date = vmState.value.heActivityForm.date,
                            saveDateMilli = vmState.value.heActivityForm.saveDateMilli
                        )
                    )
                    vmState.update { state ->
                        state.copy(
                            heActivityLoading = false
                        )
                    }
                    viewModelScope.launch {
                        _heActivityFormEvent.emit(HeActivityFormEvent.NavigateToHistoryList)
                    }
                }
            }
        }
    }

    fun onActionHeActivityForm(action: HeActivityFormAction) {
        when (action) {
            is HeActivityFormAction.ChangeAddress -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            heActivityForm = state.heActivityForm.copy(
                                address = action.address
                            )
                        )
                    }
                }
            }

            is HeActivityFormAction.ChangeAttendeeList -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            heActivityForm = state.heActivityForm.copy(
                                attendeesList = action.attendeeList
                            )
                        )
                    }
                }
            }

            is HeActivityFormAction.ChangeDate -> {
                viewModelScope.launch {
                    _date.value = action.date
                    _dateInMilli.value = action.dateInMilli
                    vmState.update { state ->
                        state.copy(
                            heActivityForm = state.heActivityForm.copy(
                                date = action.date
                            )
                        )
                    }
                }
            }

            is HeActivityFormAction.ChangeFemale -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            heActivityForm = state.heActivityForm.copy(
                                female = action.female
                            )
                        )
                    }
                }
            }

            is HeActivityFormAction.ChangeMale -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            heActivityForm = state.heActivityForm.copy(
                                male = action.male
                            )
                        )
                    }
                }
            }

            is HeActivityFormAction.ChangeVolunteer -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            heActivityForm = state.heActivityForm.copy(
                                volunteer = action.volunteer
                            )
                        )
                    }
                }
            }

            HeActivityFormAction.DatePickerClick -> {
                viewModelScope.launch {
                    _heActivityFormEvent.emit(HeActivityFormEvent.DatePickerClick)
                }
            }

            HeActivityFormAction.ErrorDialogOk -> {
                viewModelScope.launch {
                    _formState.value = formState.value.copy(
                        shouldShowErrorDialog = false
                    )
                }
            }

            HeActivityFormAction.NavigateToHistoryList -> {
                viewModelScope.launch {
                    _heActivityFormEvent.emit(HeActivityFormEvent.NavigateToHistoryList)
                }
            }

            is HeActivityFormAction.SaveDataToDb -> {

            }

            HeActivityFormAction.ShowErrorDialog -> {
                viewModelScope.launch {
                    _formState.value = formState.value.copy(
                        shouldShowErrorDialog = true
                    )
                }
            }

            HeActivityFormAction.SubmitClick -> {
                viewModelScope.launch {
                    validateForm()
                }
            }
        }
    }
}