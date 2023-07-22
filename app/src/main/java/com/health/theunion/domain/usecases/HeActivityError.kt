package com.health.theunion.domain.usecases

data class HeActivityError(
    val errorAddress: Boolean = false,
    val errorAttendee: Boolean = false,
    val errorMale: Boolean = false,
    val errorFemale: Boolean = false,
)