package com.example.bookapp

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.Book
import com.example.bookapp.BookRepository
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class BookSearchViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.bookDao()

    var query by mutableStateOf("")
    var results by mutableStateOf<List<Book>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var favorites by mutableStateOf(setOf<String>())

    private val repository = BookRepository()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favorites = dao.getAll().map { it.title }.toSet()
        }
    }

    fun searchBooks() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                results = repository.searchBooks(query)
            } catch (e: Exception) {
                error = "Błąd: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            val isFav = dao.isFavorite(book.title)
            if (isFav) {
                dao.delete(FavoriteBook(book.title, book.authorName?.firstOrNull(), book.cover_i))
            } else {
                dao.insert(FavoriteBook(book.title, book.authorName?.firstOrNull(), book.cover_i))
            }
            loadFavorites()
        }
    }

    suspend fun getFavorites(): List<Book> {
        return dao.getAll().map {
            Book(it.title, listOfNotNull(it.authorName), it.cover_i)
        }
    }
}

