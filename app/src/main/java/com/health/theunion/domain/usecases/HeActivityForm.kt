package com.health.theunion.domain.usecases

data class HeActivityForm(
    val date: String = "",
    val address: String = "",
    val volunteer: String = "",
    val attendeesList: String = "",
    val male: Int = 0,
    val female: Int = 0,
    val saveDateMilli: Long = 0L
)
