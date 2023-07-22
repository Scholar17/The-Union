package com.health.theunion.ui

import android.text.format.DateFormat
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.patient_referral.PatientReferral
import com.health.theunion.domain.usecases.ReferralFormUseCase
import com.health.theunion.repository.patient_referral.PatientReferralHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class PatientReferralDetailViewModel @Inject constructor(
    private val repo: PatientReferralHistoryRepository,
) : ViewModel() {

    private val _result = mutableStateListOf<PatientReferral>()
    val result: MutableList<PatientReferral> get() = _result

    init {
        viewModelScope.launch {
            getHistory()
        }
    }

    fun detailDateTime(milli: Long): String {
        val dateFormat = "yyyy MMM dd"
        val date = getDate(milli)
        val skeleton = DateFormat.getBestDateTimePattern(Locale.getDefault(), dateFormat)
        val formatter = SimpleDateFormat(skeleton, Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
            applyLocalizedPattern(skeleton)
        }
        return formatter.format(date.time)
    }

    private fun getDate(timeMilli: Long): Date {
        return Date(timeMilli)
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

}