package com.example.bookapp

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RatingBar(
    currentRating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Row {
        (1..5).forEach { star ->
            IconButton(onClick = { onRatingSelected(star) }) {
                Icon(
                    imageVector = if (star <= currentRating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Ocena $star",
                    tint = Color.Yellow
                )
            }
        }
    }
}
