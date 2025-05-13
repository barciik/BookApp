package com.example.bookapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun BookItem(book: Book) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = book.coverId?.let {"https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"},
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(book.title, style = MaterialTheme.typography.titleMedium)
            Text(book.authorName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
