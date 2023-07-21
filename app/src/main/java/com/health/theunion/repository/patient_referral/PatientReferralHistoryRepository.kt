package com.health.theunion.repository.patient_referral

import com.health.theunion.data.patient_referral.PatientReferral
import kotlinx.coroutines.flow.Flow

interface PatientReferralHistoryRepository {
    suspend fun addItem(patientReferral: PatientReferral)
    suspend fun deleteAllItem()
    suspend fun deleteItem(id: Long)
    suspend fun getHistoryWithDate(startDate: Long, endDate: Long): Flow<List<PatientReferral>>
    suspend fun getItems(): Flow<List<PatientReferral>>
}