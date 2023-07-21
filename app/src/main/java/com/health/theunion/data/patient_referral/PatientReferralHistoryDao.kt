package com.health.theunion.data.patient_referral

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.health.theunion.common.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientReferralHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatientReferral(patientReferral: PatientReferral)

    @Query("DELETE FROM ${Constants.REFERRAL_TABLE_NAME}")
    suspend fun deleteAllHistoryFromReferral()

    @Query("DELETE FROM ${Constants.REFERRAL_TABLE_NAME} WHERE id = :id ")
    suspend fun deleteItemHistoryFromReferral(id: Long)

    @Query("SELECT * FROM ${Constants.REFERRAL_TABLE_NAME} ORDER BY saveDateMilli DESC")
    fun getAllHistoryFromReferral() : Flow<List<PatientReferral>>

    @Query("SELECT * FROM ${Constants.REFERRAL_TABLE_NAME} WHERE saveDateMilli BETWEEN :startDate AND :endDate")
    fun getReferralHistoryWithDate(startDate: Long, endDate: Long) : Flow<List<PatientReferral>>

}