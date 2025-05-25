package com.example.bookapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteBook(
    @PrimaryKey val title: String,
    val authorName: String?,
    val cover_i: Int?
)
