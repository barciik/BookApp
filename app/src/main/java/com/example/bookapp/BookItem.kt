package com.example.bookapp

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(book: Book, viewModel: BookSearchViewModel) {
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
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Dodaj recenzję",
                    tint = Color.White
                )
            }
        }

    }
}
