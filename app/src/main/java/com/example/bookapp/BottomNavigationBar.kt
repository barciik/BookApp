package com.example.bookapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF00B0FF))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigate("search") }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Szukaj",
                tint = Color.White
            )
        }
        IconButton(onClick = { navController.navigate("favorites") }) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Ulubione",
                tint = Color.White
            )
        }
        IconButton(onClick = { navController.navigate("account") }) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Konto",
                tint = Color.White
            )
        }
    }
}

