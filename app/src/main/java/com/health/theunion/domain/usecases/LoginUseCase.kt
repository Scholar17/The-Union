package com.health.theunion.domain.usecases

import javax.inject.Inject

data class LoginUseCase @Inject constructor(
    val validator: ValidateLoginForm,
    val login: Login
)
