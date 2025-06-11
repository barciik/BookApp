package com.example.bookapp.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import com.example.bookapp.BookItem
import com.example.bookapp.BookSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(navController: NavController, viewModel: BookSearchViewModel = viewModel()) {
    val query = viewModel.query
    val results = viewModel.results
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.query = it },
            label = { Text("Szukaj książki") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF00B0FF),
                focusedBorderColor = Color(0xFF00B0FF),
                unfocusedBorderColor = Color(0xFF333333),
                focusedLabelColor = Color(0xFF00B0FF),
                unfocusedLabelColor = Color(0xFFB0B0B0),

            ),
        )

//        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchBooks() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Szukaj")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error, color = Color.Red)
            else -> Column {
                results.forEach { book ->
                    BookItem(book, viewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
    }}
}
