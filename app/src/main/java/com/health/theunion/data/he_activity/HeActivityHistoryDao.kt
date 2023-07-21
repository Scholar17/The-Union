package com.health.theunion.data.he_activity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.health.theunion.common.Constants
import com.health.theunion.data.patient_referral.PatientReferral
import kotlinx.coroutines.flow.Flow

@Dao
interface HeActivityHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeActivity(heActivity: HeActivity)

    @Query("DELETE FROM ${Constants.HE_TABLE_NAME}")
    suspend fun deleteAllHistoryFromHeActivity()

    @Query("DELETE FROM ${Constants.HE_TABLE_NAME} WHERE id = :id ")
    suspend fun deleteItemHistoryFromHeActivity(id: Long)

    @Query("SELECT * FROM ${Constants.HE_TABLE_NAME} ORDER BY saveDateMilli DESC")
    fun getAllHistoryFromHeActivity() : Flow<List<HeActivity>>

    @Query("SELECT * FROM ${Constants.HE_TABLE_NAME} WHERE saveDateMilli BETWEEN :startDate AND :endDate")
    fun getHeActivityHistoryWithDate(startDate: Long, endDate: Long) : Flow<List<HeActivity>>

}