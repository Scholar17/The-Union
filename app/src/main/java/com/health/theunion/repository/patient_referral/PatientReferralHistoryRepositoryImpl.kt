package com.health.theunion.repository.patient_referral

import com.health.theunion.data.patient_referral.PatientReferral
import com.health.theunion.data.patient_referral.PatientReferralHistoryDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatientReferralHistoryRepositoryImpl @Inject constructor(private val db: PatientReferralHistoryDatabase) :
    PatientReferralHistoryRepository {

    private val dao = db.dao()

    override suspend fun addItem(patientReferral: PatientReferral) {
        dao.insertPatientReferral(patientReferral = patientReferral)
    }

    override suspend fun deleteAllItem() {
        dao.deleteAllHistoryFromReferral()
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteItemHistoryFromReferral(id = id)
    }

    override suspend fun getHistoryWithDate(
        startDate: Long,
        endDate: Long
    ): Flow<List<PatientReferral>> {
        return dao.getReferralHistoryWithDate(startDate = startDate, endDate = endDate)
    }

    override suspend fun getItems(): Flow<List<PatientReferral>> {
        return dao.getAllHistoryFromReferral()
    }
}