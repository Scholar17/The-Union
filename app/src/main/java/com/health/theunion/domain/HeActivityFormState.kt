package com.health.theunion.domain

import com.health.theunion.domain.usecases.HeActivityError
import com.health.theunion.domain.usecases.HeActivityForm

data class HeActivityFormState(
    val heActivityLoading: Boolean = false,
    val heActivityError: HeActivityError = HeActivityError(),
    val heActivityForm: HeActivityForm = HeActivityForm(),
) {
    fun heActivityLoading() = heActivityLoading
    fun heActivityError() = heActivityError
    fun heActivityForm() = heActivityForm
}