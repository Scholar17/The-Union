package com.health.theunion.domain.usecases

import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

object FormValidator {

    private const val PWD_LENGTH = 8

    fun isVerifiedName(userName: String): Boolean {
        val result = userName.trim().isEmpty()
        return !result
    }


    fun isVerifiedPassword(password: String): Boolean {
        if (password.length > PWD_LENGTH) {
            return false
        }
        return true
    }
}