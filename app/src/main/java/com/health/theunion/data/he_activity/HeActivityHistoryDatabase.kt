package com.health.theunion.data.he_activity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HeActivity::class],
    version = 1,
    exportSchema = false
)
abstract class HeActivityHistoryDatabase : RoomDatabase() {
    abstract fun dao() : HeActivityHistoryDao
}