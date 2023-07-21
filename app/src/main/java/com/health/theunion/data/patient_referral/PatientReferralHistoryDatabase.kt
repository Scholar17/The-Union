package com.health.theunion.data.patient_referral

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PatientReferral::class],
    version = 1,
    exportSchema = false
)
abstract class PatientReferralHistoryDatabase : RoomDatabase() {
    abstract fun dao() : PatientReferralHistoryDao
}