package com.health.theunion.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.patient_referral.PatientReferral
import com.health.theunion.domain.LoginViewModelState
import com.health.theunion.domain.PatientReferralFormState
import com.health.theunion.domain.events.LoginEvent
import com.health.theunion.domain.events.PatientReferralFormEvent
import com.health.theunion.domain.usecases.LoginUseCase
import com.health.theunion.domain.usecases.ReferralFormUseCase
import com.health.theunion.presentation.PatientReferralFormAction
import com.health.theunion.repository.patient_referral.PatientReferralHistoryRepository
import com.health.theunion.ui.udf.PatientReferralState
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
class PatientReferralFormViewModel @Inject constructor(
    private val repo: PatientReferralHistoryRepository,
    private val useCase: ReferralFormUseCase,
) : ViewModel() {

    val _date = mutableStateOf(getCurrentDate())
    private val date: MutableState<String> get() = _date

    private val _patientReferralFormEvent = MutableSharedFlow<PatientReferralFormEvent>()
    val patientReferralFormEvent: SharedFlow<PatientReferralFormEvent> get() = _patientReferralFormEvent

    val _dateInMilli = mutableStateOf(getCurrentDateInMilli())
    private val dateInMilli: MutableState<Long> get() = _dateInMilli

    private val vmState = MutableStateFlow(PatientReferralFormState())

    val _formState = mutableStateOf(PatientReferralState())
    private val formState: State<PatientReferralState> get() = _formState


    val patientReferralLoading = vmState
        .map(PatientReferralFormState::patientReferralLoading)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.patientReferralLoading()
        )

    val patientReferralError = vmState
        .map(PatientReferralFormState::patientReferralError)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.patientReferralError()
        )

    val patientReferralForm = vmState
        .map(PatientReferralFormState::patientReferralForm)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.patientReferralForm()
        )

    init {
        viewModelScope.launch {
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        referDate = _date.value
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        referDateMilli = _dateInMilli.value
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        saveDateMilli = _dateInMilli.value
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        township = "AMP"
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        referFrom = "VOL1"
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        referTo = "Union"
                    )
                )
            }
            vmState.update { state ->
                state.copy(
                    referralForm = state.referralForm.copy(
                        signAndSymptom = "Fever"
                    )
                )
            }
        }
    }

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

    private fun validateForm() {
        useCase.validator(
            patientReferralForm = vmState.value.patientReferralForm()
        ).apply {
            vmState.update { state ->
                state.copy(
                    referralError = this
                )
            }
        }.also {
            if (it.errorSex) {
                viewModelScope.launch {
                    _patientReferralFormEvent.emit(PatientReferralFormEvent.ShowSnackBar("Please select sex"))
                }
            }
            if (!it.errorName && !it.errorAge && !it.errorAddress && !it.errorSex) {
                vmState.update { state ->
                    state.copy(
                        referralLoading  = true
                    )
                }
                viewModelScope.launch {
                    repo.addItem(patientReferral = PatientReferral(
                        name = vmState.value.referralForm.name,
                        sex = vmState.value.referralForm.sex,
                        age = vmState.value.referralForm.age,
                        referDate = vmState.value.referralForm.referDate,
                        township = vmState.value.referralForm.township,
                        address = vmState.value.referralForm.address,
                        referFrom = vmState.value.referralForm.referFrom,
                        referTo = vmState.value.referralForm.referTo,
                        signAndSymptom = vmState.value.referralForm.signAndSymptom,
                        referDateMilli = vmState.value.referralForm.referDateMilli,
                        saveDateMilli = vmState.value.referralForm.saveDateMilli
                    ))
                    vmState.update { state ->
                        state.copy(
                            referralLoading  = false
                        )
                    }
                    viewModelScope.launch {
                        _patientReferralFormEvent.emit(PatientReferralFormEvent.NavigateToHistoryList)
                    }
                }
            }
        }
    }

    fun onActionPatientReferralForm(action: PatientReferralFormAction) {
        when (action) {
            is PatientReferralFormAction.ChangeDate -> {
                viewModelScope.launch {
                    _date.value = action.date
                    _dateInMilli.value = action.dateInMilli
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                referDate = action.date
                            )
                        )
                    }
                }
            }
            PatientReferralFormAction.DatePickerClick -> {
                viewModelScope.launch {
                    _patientReferralFormEvent.emit(PatientReferralFormEvent.DatePickerClick)
                }
            }
            PatientReferralFormAction.ErrorDialogOk -> {
                viewModelScope.launch {
                    _formState.value = formState.value.copy(
                        shouldShowErrorDialog = false
                    )
                }
            }
            PatientReferralFormAction.NavigateToHistoryList -> TODO()
            is PatientReferralFormAction.SaveDataToDb -> TODO()
            PatientReferralFormAction.ShowErrorDialog -> {
                viewModelScope.launch {
                    _formState.value = formState.value.copy(
                        shouldShowErrorDialog = true
                    )
                }
            }
            is PatientReferralFormAction.chooseRadio -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                           referralForm = state.referralForm.copy(
                               sex = action.sex
                           )
                        )
                    }
                }
            }

            is PatientReferralFormAction.ChangeUserName -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                name = action.name
                            )
                        )
                    }
                }
            }

            is PatientReferralFormAction.ChangeAge -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                age = action.age
                            )
                        )
                    }
                }
            }

            is PatientReferralFormAction.ChangeTownship -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                township = action.township
                            )
                        )
                    }
                }
            }
            is PatientReferralFormAction.ChangeReferFrom -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                referFrom = action.referFrom
                            )
                        )
                    }
                }
            }
            is PatientReferralFormAction.ChangeReferTo -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                referTo = action.referTo
                            )
                        )
                    }
                }
            }
            is PatientReferralFormAction.ChangeSignAndSymptom -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                signAndSymptom = action.signAndSymptom
                            )
                        )
                    }
                }
            }

            is PatientReferralFormAction.ChangeAddress -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            referralForm = state.referralForm.copy(
                                address = action.address
                            )
                        )
                    }
                }
            }

            PatientReferralFormAction.SubmitClick -> {
                viewModelScope.launch {
                    validateForm()
                }
            }

        }
    }
}