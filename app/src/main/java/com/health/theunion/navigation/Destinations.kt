package com.health.theunion.navigation

object Routes {
    const val APP_ROUTE = "app"
}

object ArgConstants {
    const val ID = "db_id"
}

sealed class Destinations(val route: String) {
    object Login : Destinations(route = "login")
    object Home : Destinations(route = "home")
    object PatientReferralList : Destinations(route = "patient_referral_list")
    object PatientReferralForm : Destinations(route = "patient_referral_form")
    object PatientReferralDetail : Destinations(route = "patient_referral_detail/{${ArgConstants.ID}}") {
        fun passId(dataId: Long): String {
            return this.route.replace(
                oldValue = "{${ArgConstants.ID}}",
                newValue = dataId.toString()
            )
        }
    }
    object HeActivityList : Destinations(route = "he_activity_list")
    object HeActivityForm : Destinations(route = "he_activity_form")
    object HeActivityDetail : Destinations(route = "he_activity_detail/{${ArgConstants.ID}}") {
        fun passId(dataId: Long): String {
            return this.route.replace(
                oldValue = "{${ArgConstants.ID}}",
                newValue = dataId.toString()
            )
        }
    }
}