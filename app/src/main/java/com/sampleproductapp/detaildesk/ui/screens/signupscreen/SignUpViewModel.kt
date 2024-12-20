package com.sampleproductapp.detaildesk.ui.screens.signupscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampleproductapp.detaildesk.SignInScreen
import com.sampleproductapp.detaildesk.modal.data.SignUp
import com.sampleproductapp.detaildesk.modal.data.isValidEmail
import com.sampleproductapp.detaildesk.modal.data.isValidPassword
import com.sampleproductapp.detaildesk.modal.data.passwordMatches
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import com.sampleproductapp.detaildesk.ui.components.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sampleproductapp.detaildesk.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val detailDeskRepository: DetailDeskRepository
): ViewModel() {

//    private val _signUpResult = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
//    val signUpResult: StateFlow<SignUpUiState> = _signUpResult

    var uiState = mutableStateOf(SignUpState())
    private val userName
        get() = uiState.value.userName
    private val userEmail
        get() = uiState.value.userEmail
    private val userPassword
        get() = uiState.value.userPassword
    private val userPhoneNum
        get() = uiState.value.userPhoneNum
    private val repeatPassword
        get() = uiState.value.repeatPassword
    fun onUserNameChange(newValue: String) {
        uiState.value = uiState.value.copy(userName = newValue)
    }

    fun onUserPhoneNum(newValue: String) {
        uiState.value = uiState.value.copy(userPhoneNum = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(userEmail = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(userPassword =  newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignInClick(popUp: () -> Unit){
        popUp()
    }

    fun onSignUpClick(navigate: (Any) -> Unit) {
        if (!userEmail.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!userPassword.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!repeatPassword.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

       registerUser(
           signUp = SignUp(
               userName = userName,
               userEmail = userEmail,
               userPassword = userPassword,
               userPhoneNum = userPhoneNum
           ),
           onFailure = {},
           onSuccess = {navigate(SignInScreen)}
       )

    }
    fun registerUser(signUp: SignUp,onSuccess:()-> Unit, onFailure:()-> Unit){
        viewModelScope.launch {
            val result = detailDeskRepository.registerUser(signUp)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

}

//sealed class SignUpUiState {
//    object Idle : SignUpUiState()
////    object Loading : SignUpUiState()
////    object  Success : SignUpUiState()
//    data class Error(val message: String) : SignUpUiState()
//}