package com.health.theunion.repository.he_activity

import com.health.theunion.data.he_activity.HeActivity
import kotlinx.coroutines.flow.Flow

interface HeActivityHistoryRepository {
    suspend fun addItem(heActivity: HeActivity)
    suspend fun deleteAllItem()
    suspend fun deleteItem(id: Long)
    suspend fun getHistoryWithDate(startDate: Long, endDate: Long): Flow<List<HeActivity>>
    suspend fun getItems(): Flow<List<HeActivity>>
}