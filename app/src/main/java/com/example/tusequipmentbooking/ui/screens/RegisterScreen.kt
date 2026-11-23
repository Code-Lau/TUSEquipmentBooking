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
fun RegisterScreen(isBusy: Boolean, error: String?,
                   onRegister: (String, String) -> Unit, onCancel: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Create user account", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(email, { email = it }, label = { Text("Email") },
            enabled = !isBusy, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        OutlinedTextField(password, { password = it }, label = { Text("Password") },
            enabled = !isBusy, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        OutlinedTextField(confirm, { confirm = it }, label = { Text("Confirm password") },
            enabled = !isBusy, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            Text("Enter a valid email address.")
        }
        if (password.isNotEmpty() && password.length < 6) {
            Text("Password must be at least 6 characters.")
        }
        if (confirm.isNotEmpty() && password != confirm) {
            Text("Passwords do not match.")
        }
        error?.let { Text(it) }
        Button(enabled = !isBusy && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                password.length >= 6 && password == confirm,
            onClick = { onRegister(email, password) }) {
            Text(if (isBusy) "Creating account…" else "Register")
        }
        TextButton(enabled = !isBusy, onClick = onCancel) { Text("Cancel") }
    }
}