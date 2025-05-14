package com.example.bookapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun BookSearchScreen(viewModel: BookSearchViewModel = viewModel()) {
    val query = viewModel.query
    val results = viewModel.results
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.query = it },
            label = { Text("Szukaj książki") },
            modifier = Modifier.fillMaxWidth()
        )

//        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchBooks() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Szukaj")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error, color = Color.Red)
            else -> results.forEach { book ->
                book.cover_i?.let { id ->
                    val imageUrl = "https://covers.openlibrary.org/b/id/$id-M.jpg"
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .padding(vertical = 4.dp)
                    )
                }
                Text(book.title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                book.authorName?.firstOrNull()?.let {
                    Text("Autor: $it", textAlign = TextAlign.Center)
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
