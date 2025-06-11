package com.example.bookapp.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: FirebaseAuthViewModel = viewModel()
) {
    val email by viewModel::email
    val password by viewModel::password
    val isLogin by viewModel::isLogin
    val message by viewModel::message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (isLogin) "Logowanie" else "Rejestracja",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF00B0FF),
                focusedBorderColor = Color(0xFF00B0FF),
                unfocusedBorderColor = Color(0xFF333333),
                focusedLabelColor = Color(0xFF00B0FF),
                unfocusedLabelColor = Color(0xFFB0B0B0),
                unfocusedTextColor = Color.White
                ),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.password = it },
            label = { Text("Hasło") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF00B0FF),
                focusedBorderColor = Color(0xFF00B0FF),
                unfocusedBorderColor = Color(0xFF333333),
                focusedLabelColor = Color(0xFF00B0FF),
                unfocusedLabelColor = Color(0xFFB0B0B0),
                unfocusedTextColor = Color.White
                ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.submit {
                    navController.navigate("search") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "Zaloguj się" else "Zarejestruj się")
        }

        TextButton(onClick = { viewModel.toggleMode() }) {
            Text(if (isLogin) "Nie masz konta? Zarejestruj się" else "Masz już konto? Zaloguj się")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}

