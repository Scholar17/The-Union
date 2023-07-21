package com.health.theunion.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.health.theunion.R
import com.health.theunion.presentation.LoginAction
import com.health.theunion.ui.components.CommonPasswordTextField
import com.health.theunion.ui.components.CommonTextField
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable*/
/*
fun HomeView() {
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
                    VerticalSpacer(size = MaterialTheme.dimen.base_4x)
                    androidx.compose.material3.ListItem(modifier = Modifier.clickable { },
                    leadingContent = {}, headlineText = {Text(
                            text = stringResource(id = R.string.password),
                            color = MaterialTheme.colorScheme.error
                        )}, ) {

                    }
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
}*/
