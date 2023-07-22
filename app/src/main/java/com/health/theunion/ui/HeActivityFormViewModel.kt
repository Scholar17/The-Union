package com.health.theunion.ui

import androidx.lifecycle.ViewModel
import com.health.theunion.domain.usecases.ReferralFormUseCase
import com.health.theunion.repository.he_activity.HeActivityHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeActivityFormViewModel @Inject constructor(
    private val repo: HeActivityHistoryRepository,
    private val useCase: ReferralFormUseCase,
) : ViewModel() {

}