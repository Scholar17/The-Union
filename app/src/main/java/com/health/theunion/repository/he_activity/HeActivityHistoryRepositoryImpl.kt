package com.health.theunion.repository.he_activity

import com.health.theunion.data.he_activity.HeActivity
import com.health.theunion.data.he_activity.HeActivityHistoryDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HeActivityHistoryRepositoryImpl @Inject constructor(
    private val db: HeActivityHistoryDatabase
) : HeActivityHistoryRepository {

    private val dao = db.dao()

    override suspend fun addItem(heActivity: HeActivity) {
        dao.insertHeActivity(heActivity = heActivity)
    }

    override suspend fun deleteAllItem() {
        dao.deleteAllHistoryFromHeActivity()
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteItemHistoryFromHeActivity(id = id)
    }

    override suspend fun getHistoryWithDate(
        startDate: Long,
        endDate: Long
    ): Flow<List<HeActivity>> {
        return dao.getHeActivityHistoryWithDate(startDate = startDate, endDate = endDate)
    }

    override suspend fun getItems(): Flow<List<HeActivity>> {
        return dao.getAllHistoryFromHeActivity()
    }
}