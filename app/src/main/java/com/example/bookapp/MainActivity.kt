package com.example.bookapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookapp.auth.AuthScreen
import com.example.bookapp.screens.BookSearchScreen
import com.example.bookapp.screens.FavoriteScreen
import com.example.bookapp.ui.theme.BookAppTheme
import androidx.compose.runtime.getValue
import com.example.bookapp.screens.AccountScreen
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            BookAppTheme {
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route
                val showBottomBar = currentDestination != "auth"
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (FirebaseAuth.getInstance().currentUser == null) "auth" else "search",
//                        startDestination = "auth",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("auth") {
                            AuthScreen(navController = navController, viewModel = viewModel())
                        }
                        composable("search") {
                            BookSearchScreen(navController)
                        }
                        composable("favorites") {
                            FavoriteScreen(navController)
                        }
                        composable("account") {
                            AccountScreen(navController)
                        }

                    }
                }
            }


        }
    }
}
