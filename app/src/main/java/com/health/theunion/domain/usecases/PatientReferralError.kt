package com.health.theunion.domain.usecases

data class PatientReferralError(
    val errorName: Boolean = false,
    val errorAge: Boolean = false,
    val errorAddress: Boolean = false,
    val errorSex: Boolean = false,
)
