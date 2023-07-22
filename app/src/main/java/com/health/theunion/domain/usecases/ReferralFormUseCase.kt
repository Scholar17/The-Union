package com.health.theunion.domain.usecases

import javax.inject.Inject

data class ReferralFormUseCase @Inject constructor(
    val validator: ValidatePatientReferralForm,
)

