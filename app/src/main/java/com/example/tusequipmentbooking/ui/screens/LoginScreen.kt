package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import android.util.Patterns
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    isBusy: Boolean,
    error: String?,
    onLogin: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true, enabled = !isBusy,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        OutlinedTextField(value = password, onValueChange = { password = it },
            label = { Text("Password") }, singleLine = true, enabled = !isBusy,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            Text("Enter a valid email address.")
        }
        error?.let { Text(it) }
        Button(enabled = !isBusy && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                password.isNotBlank(),
            onClick = { onLogin(email, password) }) {
            Text(if (isBusy) "Signing in…" else "Login")
        }
        TextButton(enabled = !isBusy, onClick = onCancel) { Text("Cancel") }
    }
}
