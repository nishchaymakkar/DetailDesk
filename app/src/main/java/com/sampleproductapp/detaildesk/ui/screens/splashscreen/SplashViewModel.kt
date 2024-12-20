package com.sampleproductapp.detaildesk.ui.screens.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampleproductapp.detaildesk.HomeScreen
import com.sampleproductapp.detaildesk.SignInScreen
import com.sampleproductapp.detaildesk.SplashScreen
import com.sampleproductapp.detaildesk.modal.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {
    val sessionToken: Flow<String?> = dataStoreRepository.sessionToken

    fun onAppStart(navigate:(Any)-> Unit) {
        viewModelScope.launch {
           // Collect the session token
            val token = sessionToken.firstOrNull()

            if (token != null) {
                 navigate(HomeScreen)

            } else {
               navigate(SignInScreen) // Handle unauthenticated state
            }
        }
    }
}
