@file:OptIn(ExperimentalMaterial3Api::class)

package com.sampleproductapp.detaildesk.ui.screens.signupscreen

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sampleproductapp.detaildesk.R
import com.sampleproductapp.detaildesk.ui.components.BasicButton
import com.sampleproductapp.detaildesk.ui.components.EmailTextField
import com.sampleproductapp.detaildesk.ui.components.NameTextField
import com.sampleproductapp.detaildesk.ui.components.PasswordField
import com.sampleproductapp.detaildesk.ui.components.RepeatPasswordField

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navigate: (Any) -> Unit,
    popUp: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    SignUpContent(uiState = uiState,
        onSignUpClick = {viewModel.onSignUpClick(navigate)},
        onSignInClick = {viewModel.onSignInClick(popUp)},
        onPasswordChange = viewModel::onPasswordChange,
        onEmailChange = viewModel::onEmailChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onUserNameChange = viewModel::onUserNameChange)
}

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: SignUpState,
    onSignUpClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onUserNameChange: (String) -> Unit
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
                        Row(
                            modifier = modifier
                                .padding(start = 16.dp, end = 16.dp).fillMaxWidth()
                                .size(width = 150.dp, height = 50.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.signup),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        NameTextField(value = uiState.userName, onNewValue = onUserNameChange,modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp))
                        EmailTextField(value = uiState.userEmail, onNewValue = onEmailChange, modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp))
                        PasswordField(value = uiState.userPassword, onNewValue = onPasswordChange, modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp))
                        RepeatPasswordField(value = uiState.repeatPassword, onNewValue = onRepeatPasswordChange, modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp))

                        BasicButton(text = R.string.sign_up,modifier = modifier, action = {onSignUpClick()})
                        Row(
                            modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.alreadyhaveacc),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = modifier.size(4.dp))
                            Text(
                                text = stringResource(id = R.string.sign_in),
                                modifier = modifier.clickable(onClick = onSignInClick),
                                color = MaterialTheme.colorScheme.primary,
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