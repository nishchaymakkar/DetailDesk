@file:OptIn(ExperimentalMaterialApi::class)

package com.sampleproductapp.detaildesk.ui.screens.profilescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sampleproductapp.detaildesk.ProfileScreen
import com.sampleproductapp.detaildesk.R
import com.sampleproductapp.detaildesk.ui.components.DialogCancelButton
import com.sampleproductapp.detaildesk.ui.components.DialogConfirmButton
import com.sampleproductapp.detaildesk.R.string as AppText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    reStartApp: (Any) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    popUp: () -> Unit
) {
    ProfileScreenContent(
        modifier = modifier,
        onSignOutClick = { viewModel.onSignOutClick(reStartApp)},
        username = "",
        popUp = {popUp}
    )
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun ProfileScreenContent(
    modifier: Modifier,
    onSignOutClick: () -> Unit,
    popUp: () -> Unit,
    username: String?,
){
    Scaffold(
        modifier.fillMaxSize()
    ) { it ->
    Box(modifier
        .padding(it)
        .fillMaxSize()
       ){

    Column (
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.onPrimary),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = null,
                    modifier
                        .size(100.dp)
                        .padding(10.dp)
                )
                Text(text = stringResource(R.string.hello)+" $username!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1
                )
            }
        }

        Spacer(
            modifier
                .height(20.dp)
                .fillMaxWidth())
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Card(onClick = {onPiClick()}, modifier = modifier
//                .padding(8.dp)
//                .height(50.dp),
//                colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.onPrimary,
//            ), shape = MaterialTheme.shapes.small, elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
//            ) {
//                Row (
//                    modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    verticalAlignment = Alignment.CenterVertically){
//                    Row(horizontalArrangement = Arrangement.Start, modifier = modifier.weight(1f)) {
//                        Icon(imageVector = Icons.Default.AccountCircle,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = modifier
//                                .padding(8.dp)
//                                .size(30.dp)
//                        )
//                    }
//                    Row(horizontalArrangement = Arrangement.Center,
//                        modifier = modifier
//                            .fillMaxWidth()
//                            .weight(2f)){
//
//                        Text(text = stringResource(AppText.pi), color = MaterialTheme.colorScheme.secondary)
//                    }
//                    Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(1f)) {
//                        Icon(imageVector = Icons.Default.KeyboardArrowRight,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.secondary,
//                            modifier = modifier
//                                .padding(8.dp)
//                                .size(30.dp))
//                    }
//                }
//            }
//            Card(onClick = {/*Cards Screen*/}, modifier = modifier
//                .padding(8.dp)
//                .height(50.dp),
//                colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.onPrimary
//            ), shape = MaterialTheme.shapes.small, border = BorderStroke(
//                    color = Color.LightGray, width = 1.dp
//                )) {
//                Row (
//                    modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    verticalAlignment = Alignment.CenterVertically){
//                    Row(horizontalArrangement = Arrangement.Start, modifier = modifier.weight(1f)) {
//                        Icon(imageVector = Icons.Default.Menu,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = modifier
//                                .padding(8.dp)
//                                .size(30.dp)
//                        )
//                    }
//                    Row(horizontalArrangement = Arrangement.Center,
//                        modifier = modifier
//                            .fillMaxWidth()
//                            .weight(1f)){
//
//                        Text(text = stringResource(AppText.card))
//                    }
//                    Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(1f)) {
//                        Icon(imageVector = Icons.Default.KeyboardArrowRight,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = modifier
//                                .padding(8.dp)
//                                .size(30.dp))
//                    }
//                }
//            }
            var showWarningDialog by remember { mutableStateOf(false) }
            Card(onClick =  {  showWarningDialog = true  }, modifier = modifier
                .padding(8.dp)
                .height(50.dp),
                colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ), shape = MaterialTheme.shapes.small, elevation = CardDefaults.cardElevation(3.dp))
            {
                Row (
                    modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically){
                    Row(horizontalArrangement = Arrangement.Start, modifier = modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = modifier
                                .padding(8.dp)
                                .size(30.dp)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)){

                        Text(text = stringResource(AppText.signout), color = MaterialTheme.colorScheme.secondary)
                    }
                    Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = modifier
                                .padding(8.dp)
                                .size(30.dp))
                    }
                }
            }

            if (showWarningDialog) {
                AlertDialog(
                    title = {
                        Row(modifier.fillMaxWidth()) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(stringResource(AppText.sign_out_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary)

                        }

                    },
                    text = { Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { Text(stringResource(AppText.sign_out_description), color = Color.Gray)} },
                    dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
                    confirmButton = {
                        DialogConfirmButton(AppText.signout) {
                            onSignOutClick()
                            showWarningDialog = false
                        }
                    },
                    onDismissRequest = { showWarningDialog = false }
                )
            }
        }
    }
        Row (
            modifier = modifier
                .fillMaxWidth()
                   .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {popUp()},
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
}
}
}