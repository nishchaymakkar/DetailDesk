package com.sampleproductapp.detaildesk.ui.screens.loginscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampleproductapp.detaildesk.HomeScreen
import com.sampleproductapp.detaildesk.SignUpScreen
import com.sampleproductapp.detaildesk.modal.data.Login
import com.sampleproductapp.detaildesk.modal.data.LoginResponse
import com.sampleproductapp.detaildesk.modal.data.isValidEmail
import com.sampleproductapp.detaildesk.modal.datastore.DataStoreRepository
import com.sampleproductapp.detaildesk.modal.network.DetailDeskApiService
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import com.sampleproductapp.detaildesk.ui.components.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sampleproductapp.detaildesk.R.string as AppText

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val detailDeskRepository: DetailDeskRepository

    ) : ViewModel() {
    var uiState = mutableStateOf(SignInState())
        private set


    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val _loginResult = MutableLiveData<Result<Unit>>(null)
    val loginResult: LiveData<Result<Unit>> = _loginResult


    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState


    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }
    fun onSignInClick(openAndPopUp: (Any) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }
        viewModelScope.launch {
            val login = Login(userEmail = email, userPassword = password )

            _loginState.value = LoginUiState.Loading // Show loading indicator
            val result = detailDeskRepository.login(login)
            if (result.isSuccess) {
                val loginResponse = result.getOrNull()
                if (loginResponse != null) {
                    _loginResult.value = Result.success(Unit)
                    _loginState.value = LoginUiState.Success(loginResponse)
                    saveLoginData(loginResponse)
                    Log.d("bearer token"," ${loginResponse.sessionToken}")
                     openAndPopUp(HomeScreen)

                } else {
                    _loginResult.value = Result.failure(Exception("Login Failed"))
                    _loginState.value =
                        LoginUiState.Error("Unexpected error: Login response is null")
                }
            } else if (result.isFailure) {
                val exception = result.exceptionOrNull()
                _loginResult.value = Result.failure(Exception("Login Failed"))
                _loginState.value = LoginUiState.Error(exception?.message ?: "Login failed")
            }
        }

    }
    fun saveLoginData(login: LoginResponse) {
        viewModelScope.launch {
            dataStoreRepository.saveSessionToken(login.sessionToken,login.userId)
        }
    }
    fun onSignUpClick(navigate: (Any) -> Unit) {
        navigate(SignUpScreen)
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val login: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}