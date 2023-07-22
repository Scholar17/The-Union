package com.health.theunion.domain.usecases

import com.health.theunion.domain.HeActivityFormState
import com.health.theunion.ui.udf.HeActivityState
import javax.inject.Inject

class ValidateHeActivityForm @Inject constructor() {
    operator fun invoke(
        heActivityForm: HeActivityForm
    ): HeActivityError {

        val addressResult = FormValidator.isVerifiedName( userName =heActivityForm.address)
        val attendeeResult = FormValidator.isVerifiedName( userName = heActivityForm.attendeesList)
        val maleResult = FormValidator.isVerifiedCount( count = heActivityForm.male)
        val femaleResult = FormValidator.isVerifiedCount( count = heActivityForm.female)
        return HeActivityError(
            errorAddress = !addressResult,
            errorAttendee = !attendeeResult,
            errorMale = !maleResult,
            errorFemale = !femaleResult
        )
    }
}