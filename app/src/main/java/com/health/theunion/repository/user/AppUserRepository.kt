package com.health.theunion.repository.user

import com.health.theunion.data.user.AppUser
import kotlinx.coroutines.flow.Flow

interface AppUserRepository {
    suspend fun addItem(appUser: AppUser)
    suspend fun getItemList(): Flow<List<AppUser>>
    suspend fun deleteAllItem()
    suspend fun deleteItem(id: Long)
    suspend fun getInitialUser(): AppUser
}