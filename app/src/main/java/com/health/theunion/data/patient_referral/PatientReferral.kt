package com.health.theunion.data.patient_referral

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.health.theunion.common.Constants

@Entity(tableName = Constants.REFERRAL_TABLE_NAME
)
data class PatientReferral(
    val name: String = "",
    val sex : Int = -1,
    val age: Int = 0,
    val referDate: Long = 0L,
    val township: String = "",
    val address: String = "",
    val referFrom: String = "",
    val referTo: String = "",
    val signAndSymptom: String = "",
    val saveDateMilli: Long = 0L,
){
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null
}