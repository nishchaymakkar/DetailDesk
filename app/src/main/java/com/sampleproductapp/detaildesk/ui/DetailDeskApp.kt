package com.sampleproductapp.detaildesk.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sampleproductapp.detaildesk.AddProductScreen

import com.sampleproductapp.detaildesk.DetailDeskAppState
import com.sampleproductapp.detaildesk.HomeScreen
import com.sampleproductapp.detaildesk.ProfileScreen
import com.sampleproductapp.detaildesk.SignInScreen
import com.sampleproductapp.detaildesk.SignUpScreen
import com.sampleproductapp.detaildesk.SplashScreen
import com.sampleproductapp.detaildesk.ui.components.SnackbarManager
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.AddProductScreen
import com.sampleproductapp.detaildesk.ui.screens.homescreen.HomeScreen
import com.sampleproductapp.detaildesk.ui.screens.loginscreen.SignInScreen
import com.sampleproductapp.detaildesk.ui.screens.profilescreen.ProfileScreen
import com.sampleproductapp.detaildesk.ui.screens.signupscreen.SignUpScreen
import com.sampleproductapp.detaildesk.ui.screens.splashscreen.SplashScreen
import kotlinx.coroutines.CoroutineScope
const val FAB_EXPLODE_BOUNDS_KEY = "FAB_EXPLODE_BOUNDS_KEY"

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(
    snackbarHostState,
    navController,
    snackbarManager,
    resources,
    coroutineScope
) {
    DetailDeskAppState(
        snackbarHostState,
        navController,
        snackbarManager,
        resources,
        coroutineScope
    )
}
@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailDeskApp(){
    val appState = rememberAppState()
    val navController = rememberNavController()
    Surface {
        Scaffold (
            modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary),
            snackbarHost = {
                SnackbarHost(
                    hostState = appState.scaffoldHostState,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackbarData ->
                        Snackbar(
                            snackbarData,
                            contentColor = MaterialTheme.colorScheme.error,
                            containerColor = MaterialTheme.colorScheme.onError
                        )
                    }
                )
            },
        ){it ->
            SharedTransitionLayout {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = SplashScreen
                ){
                    composable <SplashScreen>{
                        SplashScreen(
                            onAppStart = {navigate -> navController.navigate(navigate){
                                launchSingleTop = true
                                popUpTo<SplashScreen> { inclusive = true }
                            }
                            }
                        )
                    }

                    composable <HomeScreen>{
                        HomeScreen(navigatePS = {navController.navigate(ProfileScreen)},
                            addProduct = {navController.navigate(AddProductScreen)},
                            animatedVisiblityScope = this
                        )
                    }

                    composable<ProfileScreen>{
                        ProfileScreen(reStartApp = {navigate -> navController.navigate(navigate){
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        } },
                            popUp = {
                                navController.popBackStack()
                            })
                    }

                    composable<AddProductScreen> {
                        AddProductScreen(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(
                                        key = FAB_EXPLODE_BOUNDS_KEY
                                    ),
                                    animatedVisibilityScope = this
                                ),
                            popUp = {navController.popBackStack()}
                        )
                    }
                    composable<SignInScreen>{
                        SignInScreen(navigate = {navigate -> navController.navigate(navigate){
                            launchSingleTop = true
                            popUpTo(0){ inclusive = true}
                        } })
                    }
                    composable<SignUpScreen>{
                        SignUpScreen(
                            navigate = { navigate ->
                                navController.navigate(navigate) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }, popUp = {navController.popBackStack()})
                    }

                }
            }


        }
    }
}