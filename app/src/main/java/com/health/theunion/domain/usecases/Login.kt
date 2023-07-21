package com.health.theunion.domain.usecases

import com.health.theunion.repository.user.AppUserRepository
import javax.inject.Inject

class Login @Inject constructor(
    private val userRepo: AppUserRepository
) {
    suspend operator fun invoke(
        userName: String,
        password: String,
    ): Pair<Boolean, String> {

        val initialUser = userRepo.getInitialUser()
        return when {
            userName != initialUser.userName -> Pair(first = false, second = "Username is wrong")
            password != initialUser.password -> Pair(first = false, second = "Password is wrong")
            else -> Pair(
                first = true,
                second = ""

            )
        }
    }
}