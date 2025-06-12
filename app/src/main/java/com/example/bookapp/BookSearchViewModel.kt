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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
    var ratingsMap by mutableStateOf<Map<String, Double>>(emptyMap())
    var userRatings by mutableStateOf<Map<String, Int>>(emptyMap())
    var loadedRatingsTitles = mutableSetOf<String>()



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

    fun encodeTitle(title: String): String {
        return URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
    }

    fun decodeTitle(encoded: String): String {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
    }


    fun submitRating(bookTitle: String, rating: Int) {
        val userId = auth.currentUser?.uid ?: return
        val encodedTitle = encodeTitle(bookTitle)

        val ratingData = mapOf(
            "userId" to userId,
            "rating" to rating,
            "title" to bookTitle
        )

        firestore.collection("ratings")
            .document(encodedTitle)
            .collection("userRatings")
            .document(userId)
            .set(ratingData)
            .addOnSuccessListener {
                userRatings = userRatings.toMutableMap().apply {
                    put(bookTitle, rating)
                }
                updateAverageRating(bookTitle)
            }
    }



    fun loadUserRating(bookTitle: String) {
        val userId = auth.currentUser?.uid ?: return
        val encodedTitle = encodeTitle(bookTitle)

        firestore.collection("ratings")
            .document(encodedTitle)
            .collection("userRatings")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val rating = document.getLong("rating")?.toInt() ?: 0
                userRatings = userRatings.toMutableMap().apply {
                    put(bookTitle, rating)
                }
            }
    }



    fun updateAverageRating(bookTitle: String) {
        val encodedTitle = encodeTitle(bookTitle)

        firestore.collection("ratings")
            .document(encodedTitle)
            .collection("userRatings")
            .get()
            .addOnSuccessListener { result ->
                val ratings = result.mapNotNull { it.getLong("rating")?.toInt() }
                if (ratings.isNotEmpty()) {
                    val avg = ratings.average()
                    ratingsMap = ratingsMap.toMutableMap().apply {
                        put(bookTitle, avg)
                    }
                }
            }
    }

    fun loadAllUserRatings() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collectionGroup("users")
            .whereEqualTo(FieldPath.documentId(), userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val result = mutableMapOf<String, Int>()
                for (doc in snapshot) {
                    val parentBookTitle = doc.reference.parent.parent?.id
                    val rating = doc.getLong("rating")?.toInt()
                    if (parentBookTitle != null && rating != null) {
                        result[parentBookTitle] = rating
                    }
                }
                userRatings = result
            }
    }


}

