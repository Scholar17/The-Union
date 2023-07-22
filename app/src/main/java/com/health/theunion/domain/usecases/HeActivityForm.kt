package com.health.theunion.domain.usecases

data class HeActivityForm(
    val date: String = "",
    val address: String = "",
    val volunteer: String = "",
    val attendeesList: String = "",
    val male: Int = -1,
    val female: Int = -1,
    val saveDateMilli: Long = 0L
)
