package com.sampleproductapp.detaildesk.ui.screens.splashscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun SplashScreen(
    onAppStart: (Any) -> Unit,
   // openAndPopUp: (Any,Any) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    SplashScreenContent(
        onAppStart = {
         viewModel.onAppStart(onAppStart)
        }
    )
}

@Composable
fun SplashScreenContent(modifier: Modifier = Modifier,onAppStart: () -> Unit) {
    LaunchedEffect(true) {
        //delay(SPlASH_TIMEOUT)
        onAppStart()
    }
    Box(
        modifier = modifier.fillMaxSize()
    ){

    }
}