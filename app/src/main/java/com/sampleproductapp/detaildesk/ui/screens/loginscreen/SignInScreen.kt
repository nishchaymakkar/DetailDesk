@file:OptIn(ExperimentalMaterial3Api::class)

package com.sampleproductapp.detaildesk.ui.screens.loginscreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sampleproductapp.detaildesk.R
import com.sampleproductapp.detaildesk.ui.components.BasicButton
import com.sampleproductapp.detaildesk.ui.components.EmailTextField
import com.sampleproductapp.detaildesk.ui.components.PasswordField
import com.sampleproductapp.detaildesk.ui.theme.DetailDeskTheme

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navigate: (Any) -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current
    val loginResult by viewModel.loginResult.observeAsState()

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Successfully Log In", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Log In failed please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
    SignInScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        modifier = modifier,
        onSignUpClick = { viewModel.onSignUpClick(navigate) },
        onSignInClick = {viewModel.onSignInClick(navigate)}
    )
}

@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignInState,
    onEmailChange: (String) -> Unit,
    onSignUpClick: () -> Unit ,
    onSignInClick: () -> Unit ,
    onPasswordChange: (String) -> Unit
    ) {
    Scaffold(
        content = { it ->
            Box(
                modifier = modifier
                    .padding(it)
                    .fillMaxSize(),
                content = {
                    Column(
                        modifier = modifier.align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            Text(text = "Sign In")
                        }
                        EmailTextField(value = uiState.email, onNewValue = onEmailChange, modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp))
                        PasswordField(value = uiState.password, onNewValue = onPasswordChange, modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp))
                        BasicButton(text = R.string.sign_in,modifier = modifier, action = {onSignInClick()})
                        Row(
                            modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.donthaveanacc),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = modifier.size(4.dp))
                            Text(
                                text = stringResource(id = R.string.signup),
                                modifier = modifier.clickable(onClick = onSignUpClick),
                                //color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
        }
    )
}

@Preview
@Composable
private fun SignInScreenPreview() {
    val uiState = SignInState()
    DetailDeskTheme {
        SignInScreenContent(
            uiState = uiState,
            onEmailChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onSignUpClick = {}
        )
    }
}