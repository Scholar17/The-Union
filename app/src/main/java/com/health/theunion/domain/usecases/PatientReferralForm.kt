package com.health.theunion.domain.usecases

data class PatientReferralForm(
    val name: String = "",
    val sex: Int = -1,
    val age: Int = 0,
    val referDate: String = "",
    val township: String = "",
    val address: String = "",
    val referFrom: String = "",
    val referTo: String = "",
    val signAndSymptom: String = "",
    val referDateMilli: Long = 0L,
    val saveDateMilli: Long = 0L,
)
