package com.example.bookapp

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.Book
import com.example.bookapp.BookRepository
import kotlinx.coroutines.launch

class BookSearchViewModel : ViewModel() {

    private val repository = BookRepository()

    var query by mutableStateOf("")
    var results by mutableStateOf<List<Book>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun searchBooks() {
        println("123")
        if (query.isBlank()) return
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = repository.searchBooks(query)
                results = response.docs
            } catch (e: Exception) {
                error = "Błąd: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
