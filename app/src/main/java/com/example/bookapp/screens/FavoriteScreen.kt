package com.example.bookapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookapp.BookItem
import com.example.bookapp.BookSearchViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun FavoriteScreen(navController: NavController, viewModel: BookSearchViewModel = viewModel()) {
    val favorites = viewModel.favorites

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(16.dp)
        ) {
            items(favorites) { book ->
                BookItem(book, viewModel)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
