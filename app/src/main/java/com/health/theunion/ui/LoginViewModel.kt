package com.health.theunion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.theunion.data.user.AppUser
import com.health.theunion.domain.LoginViewModelState
import com.health.theunion.domain.events.LoginEvent
import com.health.theunion.domain.usecases.LoginUseCase
import com.health.theunion.presentation.LoginAction
import com.health.theunion.repository.user.AppUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
    private val userRepo: AppUserRepository,
) : ViewModel() {
    private val vmState = MutableStateFlow(LoginViewModelState())
    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent: SharedFlow<LoginEvent> get() = _loginEvent

    init {
        viewModelScope.launch {
            userRepo.deleteAllItem()
            userRepo.addItem(appUser = AppUser(userName = "Admin", password = "Admin123"))
        }
    }

    val loginLoading = vmState
        .map(LoginViewModelState::loginLoading)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.loginLoading()
        )

    val loginError = vmState
        .map(LoginViewModelState::loginError)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.loginError()
        )

    val loginForm = vmState
        .map(LoginViewModelState::loginForm)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.loginForm()
        )

    private fun validateLogin() {
        useCase.validator(
            loginForm = vmState.value.loginForm
        ).apply {
            vmState.update { state ->
                state.copy(
                    loginError = this
                )
            }
        }.also {
            if (!it.errorName && !it.errorPassword) {
                vmState.update { state ->
                    state.copy(
                        loginLoading = true
                    )
                }
                viewModelScope.launch {
                    useCase.login(
                        userName = vmState.value.loginForm.userName,
                        password = vmState.value.loginForm.password
                    ).also { result ->
                        vmState.update { state ->
                            state.copy(
                                loginLoading = false
                            )
                        }
                        when (result.first) {
                            false -> {
                                _loginEvent.emit(
                                    value = LoginEvent.ShowSnackBar(
                                        message = result.second
                                    )
                                )
                            }

                            true -> {
                                _loginEvent.emit(
                                    value = LoginEvent.NavigateToHomeScreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateLoginPassword(password: String) {
        vmState.update { state ->
            state.copy(
                loginForm = this.loginForm.value.copy(
                    password = password
                ),
                loginError = this.loginError.value.copy(
                    errorPassword = false
                )
            )
        }
    }

    private fun updateUserName(name: String) {
        vmState.update { state ->
            state.copy(
                loginForm = this.loginForm.value.copy(
                    userName = name
                ),
                loginError = this.loginError.value.copy(
                    errorName = false
                )
            )
        }
    }

    fun onActionLogin(action: LoginAction) {
        when (action) {

            LoginAction.ClickLogin -> validateLogin()

            is LoginAction.ChangePassword -> {
                updateLoginPassword(action.password)
            }

            is LoginAction.ChangeUserName -> {
                updateUserName(action.name)
            }
        }
    }


}