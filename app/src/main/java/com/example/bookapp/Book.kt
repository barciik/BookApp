package com.example.bookapp

data class Book(
    val title: String = "",
    val author_name: List<String>? = null,
    val cover_i: Int? = null,
)

data class SearchResponse(
    val docs: List<Book>
)
