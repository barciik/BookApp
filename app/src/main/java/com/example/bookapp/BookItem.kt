package com.example.bookapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(book: Book, viewModel: BookSearchViewModel) {
    val userRating = viewModel.userRatings[book.title] ?: 0
    val avgRating = viewModel.ratingsMap[book.title] ?: 0.0
    LaunchedEffect(book.title) {
        if (!book.title.isNullOrBlank() && !viewModel.loadedRatingsTitles.contains(book.title)) {
            viewModel.loadedRatingsTitles.add(book.title)
            viewModel.loadUserRating(book.title)
            viewModel.updateAverageRating(book.title)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        book.cover_i?.let { id ->
            val imageUrl = "https://covers.openlibrary.org/b/id/$id-M.jpg"
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        Text(book.title, fontWeight = FontWeight.Bold, color = Color.White)


        book.author_name?.firstOrNull()?.let {
            Text("Autor: $it", color = Color.LightGray)
        }

        Row {
            IconButton(onClick = { viewModel.toggleFavorite(book) }) {
                val isFav = viewModel.favorites.any { it.title == book.title }
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Lubię to",
                    tint = if (isFav) Color.Red else Color.White
                )
            }
//
        }
        RatingBar(
            currentRating = userRating,
            onRatingSelected = { viewModel.submitRating(book.title, it) }
        )
        Text("Średnia ocena: ${"%.1f".format(avgRating)}", color = Color.Yellow)
    }
}
