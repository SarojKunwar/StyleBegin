package com.example.stylebegin.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileEditScreen(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance() // Firebase Auth instance
    val db = FirebaseFirestore.getInstance() // Firestore instance
val context= LocalContext.current
    var currentUser by remember { mutableStateOf<FirebaseUser?>(null) } // Current user

    LaunchedEffect(Unit) {
        currentUser = auth.currentUser
    }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Surface(color = Color(0xFF253334)) {
        Column {
            // Edit section
            EditSection(
                username = username,
                onUsernameChange = { username = it },
                email = email,
                onEmailChange = { email = it },
                onSaveEditSection = {
                    currentUser?.let { user ->
                        val userRef = db.collection("users").document(user.uid)
                        userRef.update("username", username)
                        userRef.update("email", email)
                            .addOnSuccessListener {
                                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                                navHostController.navigate("home")
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context,"${e.localizedMessage}",Toast.LENGTH_SHORT).show()
                            }
                    }

                }
            )
        }
    }
}

@Composable
fun EditSection(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    onSaveEditSection: () -> Unit
) {
    Surface(modifier = Modifier.padding(24.dp).clip(RoundedCornerShape(24.dp))) {
        Box(modifier = Modifier.padding(12.dp)) {
            Column {
                Text("Edit Profile", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onSaveEditSection) {
                    Text("Save")
                }
            }
        }
    }
}
