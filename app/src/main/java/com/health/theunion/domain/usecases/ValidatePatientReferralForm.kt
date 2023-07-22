package com.health.theunion.domain.usecases

import javax.inject.Inject

class ValidatePatientReferralForm @Inject constructor() {
    operator fun invoke(
        patientReferralForm: PatientReferralForm
    ): PatientReferralError {
        val ageResult = FormValidator.isVerifiedAge( age = patientReferralForm.age)
        val nameResult = FormValidator.isVerifiedName( userName = patientReferralForm.name)
        val addressResult = FormValidator.isVerifiedName( userName = patientReferralForm.address)
        val sexResult = FormValidator.isVerifiedSex(sex = patientReferralForm.sex)
        return PatientReferralError(
            errorAge = !ageResult,
            errorName = !nameResult,
            errorAddress = !addressResult,
            errorSex = !sexResult
        )
    }
}