package com.example.tusequipmentbooking.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val uid: String? = null,
    val email: String? = null,
    val isBusy: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val mutableState = MutableStateFlow(AuthUiState())
    val uiState = mutableState.asStateFlow()

    private val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        mutableState.update { it.copy(
            uid = user?.uid, email = user?.email, error = null
        ) }
    }
    init { auth.addAuthStateListener(listener) }

    fun login(email: String, password: String) {
        if (mutableState.value.isBusy) return
        mutableState.update { it.copy(isBusy = true, error = null) }
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                mutableState.update { it.copy(isBusy = false,
                    error = if (task.isSuccessful) it.error
                    else if (task.exception is FirebaseNetworkException)
                        "No internet connection. Login needs internet access."
                    else "Sign in failed. Check your email and password.") }
            }
    }

    fun register(email: String, password: String) {
        if (mutableState.value.isBusy) return
        mutableState.update { it.copy(isBusy = true, error = null) }
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                mutableState.update { it.copy(isBusy = false,
                    error = if (task.isSuccessful) it.error
                    else if (task.exception is FirebaseNetworkException)
                        "No internet connection. Registration needs internet access."
                    else "Registration failed. Check details and password policy.") }
            }
    }

    fun logout() {
        if (mutableState.value.isBusy) return
        auth.signOut()
        mutableState.value = AuthUiState()
    }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
        super.onCleared()
    }
}
