package com.health.theunion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.health.theunion.R
import com.health.theunion.domain.events.HomeEvent
import com.health.theunion.navigation.Destinations
import com.health.theunion.presentation.HomeAction
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeView(
    vm: HomeViewModel,
    navController: NavController
) {

    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        vm.homeEvent.collectLatest {
            when (it) {
                HomeEvent.NavigateToPatientReferral -> {
                    navController.navigate(Destinations.PatientReferralList.route)
                }

                HomeEvent.NavigateToHeActivity -> {
                    navController.navigate(Destinations.HeActivityList.route)
                }

                is HomeEvent.ShowSnackBar -> {
                    snackState.showSnackbar(
                        message = it.message
                    )
                }

                else -> {
                    navController.navigate(Destinations.Login.route)
                }
            }
        }
    }
    Scaffold(
        contentWindowInsets = WindowInsets(
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        snackbarHost = {
            SnackbarHost(snackState)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimen.base_2x)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalSpacer(size = MaterialTheme.dimen.base_4x)
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                vm.onActionHome(
                                    action = HomeAction.ClickPatientReferral
                                )
                            }
                            .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
                        leadingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.patient_referral),
                                contentDescription = "Patient Referral"
                            )
                        },
                        headlineText = {
                            Text(
                                text = stringResource(id = R.string.patient_referral),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        trailingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_small_chevron_right),
                                contentDescription = "Next Arrow"
                            )
                        }
                    )
                    VerticalSpacer(size = MaterialTheme.dimen.base)
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                vm.onActionHome(
                                    action = HomeAction.ClickHeActivity
                                )
                            }
                            .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
                        leadingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.he_activity),
                                contentDescription = "He Activity"
                            )
                        },
                        headlineText = {
                            Text(
                                text = stringResource(id = R.string.he_activity),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        trailingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_small_chevron_right),
                                contentDescription = "Next Arrow"
                            )
                        }
                    )
                    VerticalSpacer(size = MaterialTheme.dimen.base)
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                vm.onActionHome(
                                    action = HomeAction.ClickLogout
                                )
                            }
                            .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                        leadingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        headlineText = {
                            Text(
                                text = stringResource(id = R.string.logout),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        trailingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_small_chevron_right),
                                contentDescription = "Next Arrow"
                            )
                        }
                    )
                }
            }
        }
    )
}

