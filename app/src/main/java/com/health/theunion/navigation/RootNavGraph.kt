package com.health.theunion.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.health.theunion.ui.HeActivityDetail
import com.health.theunion.ui.HeActivityDetailViewModel
import com.health.theunion.ui.HeActivityForm
import com.health.theunion.ui.HeActivityFormViewModel
import com.health.theunion.ui.HeActivityListView
import com.health.theunion.ui.HeActivityListViewModel
import com.health.theunion.ui.HomeView
import com.health.theunion.ui.HomeViewModel
import com.health.theunion.ui.LoginView
import com.health.theunion.ui.LoginViewModel
import com.health.theunion.ui.PatientReferralDetail
import com.health.theunion.ui.PatientReferralDetailViewModel
import com.health.theunion.ui.PatientReferralForm
import com.health.theunion.ui.PatientReferralFormViewModel
import com.health.theunion.ui.PatientReferralListView
import com.health.theunion.ui.PatientReferralListViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Login.route,
        route = Routes.APP_ROUTE
    ) {
        composable(route = Destinations.Login.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<LoginViewModel>(parentEntry)
            LoginView(vm = appViewModel, navController = navController)
        }
        composable(route = Destinations.Home.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<HomeViewModel>(parentEntry)
            HomeView(navController = navController, vm = appViewModel)
        }
        composable(route = Destinations.PatientReferralList.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<PatientReferralListViewModel>(parentEntry)
            PatientReferralListView(navController = navController, vm = appViewModel)
        }
        composable(route = Destinations.PatientReferralForm.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<PatientReferralFormViewModel>(parentEntry)
            PatientReferralForm(navController = navController, vm = appViewModel)
        }
        composable(route = Destinations.PatientReferralDetail.route,
            arguments = listOf(
                navArgument(name = ArgConstants.ID) {
                    type = NavType.LongType
                }
            )) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<PatientReferralDetailViewModel>(parentEntry)
            PatientReferralDetail(
                navController = navController,
                vm = appViewModel,
                id = it.arguments?.getLong(ArgConstants.ID) ?: 0L
            )
        }

        composable(route = Destinations.HeActivityList.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<HeActivityListViewModel>(parentEntry)
            HeActivityListView(navController = navController, vm = appViewModel)
        }
        composable(route = Destinations.HeActivityForm.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<HeActivityFormViewModel>(parentEntry)
            HeActivityForm(navController = navController, vm = appViewModel)
        }
        composable(route = Destinations.HeActivityDetail.route,
            arguments = listOf(
                navArgument(name = ArgConstants.ID) {
                    type = NavType.LongType
                }
            )) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.APP_ROUTE)
            }
            val appViewModel = hiltViewModel<HeActivityDetailViewModel>(parentEntry)
            HeActivityDetail(
                navController = navController,
                vm = appViewModel,
                id = it.arguments?.getLong(ArgConstants.ID) ?: 0L
            )
        }


    }
}