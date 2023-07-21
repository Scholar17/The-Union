package com.health.theunion.data.he_activity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.health.theunion.common.Constants

@Entity(tableName = Constants.HE_TABLE_NAME
)
data class HeActivity(
    val date: String = "",
    val address : String = "",
    val volunteer: String = "",
    val heAttendeesList: String = "",
    val sex: Int = -1,
    val saveDateMilli: Long = 0L
){
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null
}