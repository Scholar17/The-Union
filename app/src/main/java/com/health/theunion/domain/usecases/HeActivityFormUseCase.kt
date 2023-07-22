package com.health.theunion.domain.usecases

import javax.inject.Inject

data class HeActivityFormUseCase @Inject constructor(
    val validator: ValidateHeActivityForm,
)
