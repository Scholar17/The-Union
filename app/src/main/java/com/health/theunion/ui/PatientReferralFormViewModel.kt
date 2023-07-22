package com.health.theunion.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.domain.events.PatientReferralFormEvent
import com.health.theunion.domain.usecases.LoginUseCase
import com.health.theunion.presentation.PatientReferralFormAction
import com.health.theunion.repository.user.AppUserRepository
import com.health.theunion.ui.udf.PatientReferralFormState
import com.health.theunion.ui.udf.PatientReferralListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class PatientReferralFormViewModel @Inject constructor() : ViewModel() {

    val _date = mutableStateOf(getCurrentDate())
    private val date: MutableState<String> get() = _date

    private val _patientReferralFormEvent = MutableSharedFlow<PatientReferralFormEvent>()
    val patientReferralFormEvent: SharedFlow<PatientReferralFormEvent> get() = _patientReferralFormEvent

    val _dateInMilli = mutableStateOf(getCurrentDateInMilli())
    private val dateInMilli: MutableState<Long> get() = _dateInMilli

    val _formState = mutableStateOf(PatientReferralFormState())
    private val formState: State<PatientReferralFormState> get() = _formState

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

    fun onActionPatientReferralForm(action: PatientReferralFormAction) {
        when (action) {
            is PatientReferralFormAction.ChangeDate -> {

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
            is PatientReferralFormAction.chooseRadio -> TODO()
        }
    }
}