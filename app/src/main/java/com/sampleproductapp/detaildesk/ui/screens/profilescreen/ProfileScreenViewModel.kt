package com.sampleproductapp.detaildesk.ui.screens.profilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampleproductapp.detaildesk.SplashScreen
import com.sampleproductapp.detaildesk.modal.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel(){
    fun onSignOutClick(restartApp: (Any) -> Unit) {
        viewModelScope.launch {
            dataStoreRepository.clearPreferences()
            restartApp(SplashScreen)
        }

    }

}