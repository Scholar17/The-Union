package com.health.theunion.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.health.theunion.common.Constants

@Entity(tableName = Constants.USER_TABLE_NAME
)
data class AppUser(
    val userName: String = "",
    val password : String = "",
){
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null
}
