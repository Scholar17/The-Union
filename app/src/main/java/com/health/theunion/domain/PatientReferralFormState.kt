package com.health.theunion.domain

import com.health.theunion.domain.usecases.PatientReferralError
import com.health.theunion.domain.usecases.PatientReferralForm

data class PatientReferralFormState(
    val referralLoading: Boolean = false,
    val referralError: PatientReferralError = PatientReferralError(),
    val referralForm: PatientReferralForm = PatientReferralForm(

    ),
) {
    fun patientReferralLoading() = referralLoading
    fun patientReferralError() = referralError
    fun patientReferralForm() = referralForm
}
