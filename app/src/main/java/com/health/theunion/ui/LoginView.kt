package com.health.theunion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.health.theunion.R
import com.health.theunion.domain.events.LoginEvent
import com.health.theunion.presentation.LoginAction
import com.health.theunion.ui.components.CommonPasswordTextField
import com.health.theunion.ui.components.CommonTextField
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginView(
) {
    val vm: LoginViewModel = hiltViewModel()
    val loginError = vm.loginError.collectAsState()
    val loginLoading = vm.loginLoading.collectAsState()
    val loginForm = vm.loginForm.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackState = remember { SnackbarHostState() }


    LaunchedEffect(key1 = true) {
        vm.loginEvent.collectLatest {
            when (it) {
                is LoginEvent.ShowSnackBar -> {
                    snackState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }


    Scaffold(
        contentWindowInsets = WindowInsets(
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            )
        },
        snackbarHost = {
            SnackbarHost(snackState)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimen.base_2x)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalSpacer(size = MaterialTheme.dimen.base_8x)
                    Text(
                        text = stringResource(id = R.string.app_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    VerticalSpacer(size = MaterialTheme.dimen.base_4x)
                    CommonTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChanged = {
                            vm.onActionLogin(
                                LoginAction.ChangeUserName(
                                    name = it
                                )
                            )
                        },
                        placeholder = stringResource(id = R.string.user_name),
                        value = loginForm.value.userName,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        onValueCleared = {
                            vm.onActionLogin(
                                LoginAction.ChangeUserName(
                                    name = ""
                                )
                            )
                        },
                        isError = loginError.value.errorName,
                        errorMessage = stringResource(id = R.string.name_empty)
                    )
                    VerticalSpacer(size = MaterialTheme.dimen.base)
                    CommonPasswordTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChanged = {
                            vm.onActionLogin(
                                LoginAction.ChangePassword(
                                    password = it
                                )
                            )
                        },
                        placeholder = stringResource(id = R.string.password),
                        password = loginForm.value.password,
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                        isError = loginError.value.errorPassword,
                        errorMessage = stringResource(id = R.string.password_length_error),
                        keyboardAction = { keyboardController!!.hide() }
                    )
                    VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.width(MaterialTheme.dimen.base_12x),
                            onClick = {
                                vm.onActionLogin(
                                    LoginAction.ClickLogin
                                )
                                keyboardController!!.hide()
                            },
                            shape = CircleShape,
                        ) {
                            Text(text = stringResource(id = R.string.login))
                        }
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun LoginViewPreview() {
    LoginView()
}