package com.example.bookapp

data class Book(
    val title: String = "",
    val authorName: List<String>? = null,
    val cover_i: Int? = null
)

data class SearchResponse(
    val docs: List<Book>
)
