package com.example.bookapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteBook>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: FavoriteBook)

    @Delete
    suspend fun delete(book: FavoriteBook)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE title = :title)")
    suspend fun isFavorite(title: String): Boolean
}

