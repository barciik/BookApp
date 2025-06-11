package com.example.bookapp.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class FirebaseAuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLogin by mutableStateOf(true)
    var message by mutableStateOf<String?>(null)


    fun toggleMode() {
        isLogin = !isLogin
        message = null
    }

    fun submit(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            message = "Email i hasło nie mogą być puste"
            return
        }

        if (isLogin) {
            signIn(onSuccess)
        } else {
            register(onSuccess)
        }
    }

    fun logout(onSuccess: () -> Unit) {
        signOut(onSuccess)
    }

    private fun signIn(onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    message = "Zalogowano pomyślnie"
                    onSuccess()
                } else {
                    message = "Błąd logowania: ${task.exception?.message}"
                }
            }
    }

    private fun register(onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    message = "Rejestracja udana"
                    onSuccess()
                } else {
                    message = "Błąd rejestracji: ${task.exception?.message}"
                }
            }
    }
    private fun signOut(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }


}
