package com.health.theunion.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.health.theunion.common.Constants
import com.health.theunion.data.he_activity.HeActivity
import kotlinx.coroutines.flow.Flow


@Dao
interface AppUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(appUser: AppUser)

    @Query("SELECT * FROM ${Constants.USER_TABLE_NAME}")
    fun getUserList(): Flow<List<AppUser>>

    @Query("DELETE FROM ${Constants.USER_TABLE_NAME} WHERE id = :id ")
    suspend fun deleteUser(id: Long)

    @Query("DELETE FROM ${Constants.USER_TABLE_NAME}")
    suspend fun deleteAllFromUser()

    @Query("SELECT * FROM ${Constants.USER_TABLE_NAME} WHERE userName = :name")
    fun getInitialUser(name : String) : AppUser


}