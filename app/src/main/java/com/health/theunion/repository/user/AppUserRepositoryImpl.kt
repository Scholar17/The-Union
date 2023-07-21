package com.health.theunion.repository.user

import com.health.theunion.data.user.AppUser
import com.health.theunion.data.user.AppUserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.PrimitiveIterator
import javax.inject.Inject

class AppUserRepositoryImpl @Inject constructor(
    private val db: AppUserDatabase

) : AppUserRepository {

    private val dao = db.dao()

    override suspend fun addItem(appUser: AppUser) {
        dao.insertUser(appUser = appUser)
    }

    override suspend fun getItemList(): Flow<List<AppUser>> {
        return dao.getUserList()
    }

    override suspend fun deleteAllItem() {
        dao.deleteAllFromUser()
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteUser(id = id)
    }

    override suspend fun getInitialUser(): AppUser {
        return withContext(Dispatchers.IO){
            dao.getInitialUser(name = "Admin")
        }
    }
}
