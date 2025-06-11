package com.example.bookapp

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.Book
import com.example.bookapp.BookRepository
import kotlinx.coroutines.launch
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class BookSearchViewModel(application: Application) : AndroidViewModel(application) {
//    private val db = AppDatabase.getDatabase(application)
//    private val dao = db.bookDao()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    var query by mutableStateOf("")
    var results by mutableStateOf<List<Book>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var favorites by mutableStateOf<List<Book>>(emptyList())

    private val repository = BookRepository()

    init {
        loadFavoritesFromFirebase()
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

//    private fun loadFavorites() {
//        viewModelScope.launch {
//            favorites = dao.getAll().map { it.title }.toSet()
//        }
//    }

//    fun toggleFavorite(book: Book) {
//        viewModelScope.launch {
//            val isFav = dao.isFavorite(book.title)
//            if (isFav) {
//                dao.delete(FavoriteBook(book.title, book.authorName?.firstOrNull(), book.cover_i))
//            } else {
//                dao.insert(FavoriteBook(book.title, book.authorName?.firstOrNull(), book.cover_i))
//            }
//            loadFavorites()
//        }
//    }

    fun toggleFavorite(book: Book) {
        val userId = auth.currentUser?.uid ?: return
        val currentFavorites = favorites.toMutableList()

        val existingIndex = currentFavorites.indexOfFirst { it.title == book.title }
        if (existingIndex != -1) {
            currentFavorites.removeAt(existingIndex)
        } else {
            currentFavorites.add(book)
        }

        firestore.collection("favorites").document(userId)
            .set(mapOf("favorites" to currentFavorites))
            .addOnSuccessListener {
                favorites = currentFavorites
            }
            .addOnFailureListener {
                e -> {
                    e.message
            }
            }
    }


    fun loadFavoritesFromFirebase() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val document = firestore.collection("favorites").document(userId).get().await()
                val wrapper = document.toObject(FavoriteWrapper::class.java)
                favorites = wrapper?.favorites ?: emptyList()
            } catch (e: Exception) {
                favorites = emptyList()
                Log.e("Firestore", "Błąd: ${e.localizedMessage}")
            }
        }
    }

    data class FavoriteWrapper(
        val favorites: List<Book> = emptyList()
    )




}

