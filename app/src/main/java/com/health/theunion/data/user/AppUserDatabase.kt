package com.health.theunion.data.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AppUser::class],
    version = 1,
    exportSchema = false
)
abstract class AppUserDatabase : RoomDatabase() {
    abstract fun dao(): AppUserDao
}