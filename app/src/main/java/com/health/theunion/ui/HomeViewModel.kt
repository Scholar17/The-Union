package com.health.theunion.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.patient_referral.PatientReferral
import com.health.theunion.domain.events.HomeEvent
import com.health.theunion.domain.events.LoginEvent
import com.health.theunion.domain.usecases.LoginUseCase
import com.health.theunion.presentation.HomeAction
import com.health.theunion.presentation.LoginAction
import com.health.theunion.repository.patient_referral.PatientReferralHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent: SharedFlow<HomeEvent> get() = _homeEvent

    fun onActionHome(action: HomeAction) {
        viewModelScope.launch {
        when (action) {
            HomeAction.ClickPatientReferral -> {
                _homeEvent.emit(
                    value = HomeEvent.NavigateToPatientReferral
                )
            }

            HomeAction.ClickHeActivity -> {
                _homeEvent.emit(
                    value = HomeEvent.NavigateToHeActivity
                )
            }

            HomeAction.ClickLogout -> {
                _homeEvent.emit(
                    value = HomeEvent.NavigateToLogin
                )
            }
        }
        }
    }
}