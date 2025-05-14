package com.example.bookapp


class BookRepository {
    suspend fun searchBooks(query: String) = RetrofitInstance.api.searchBooks(query)
}
