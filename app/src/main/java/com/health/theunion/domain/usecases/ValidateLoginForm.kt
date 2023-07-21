package com.health.theunion.domain.usecases

import javax.inject.Inject

class ValidateLoginForm @Inject constructor() {
    operator fun invoke(
        loginForm: LoginForm
    ): LoginError {
        val phoneNumberResult = FormValidator.isVerifiedName( userName = loginForm.userName)
        val passwordResult = FormValidator.isVerifiedPassword(password = loginForm.password)
        return LoginError(
            errorName = !phoneNumberResult,
            errorPassword = !passwordResult
        )
    }
}
