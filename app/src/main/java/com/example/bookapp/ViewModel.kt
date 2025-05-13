package com.example.bookapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Book(val key: String, val authorName: String, val title: String, val coverId: String?)

class SearchViewModel : ViewModel() {
    var query by mutableStateOf("")
    var books by mutableStateOf<List<Book>>(emptyList())
    var isLoading by mutableStateOf(false)

    fun searchBooks() {
        viewModelScope.launch {
            isLoading = true


            delay(1000)
            books = listOf(
                Book("1", "Adam Mickiewicz", "Pan Tadeusz", "14625765"),
                Book("1", "Adam Mickiewicz", "Pan Tadeusz", "14625765"),
                Book("1", "Adam Mickiewicz", "Pan Tadeusz", "14625765")
            ).filter { it.title.contains(query, ignoreCase = true) }

            isLoading = false
        }
    }
}