package com.example.stylebegin.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

import androidx.navigation.NavController
import com.example.stylebegin.ui.PreferencesManager
import com.example.stylebegin.ui.api.ApiUtilities
import com.example.stylebegin.ui.components.CustomTextField
import com.example.stylebegin.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val preferencesManager = PreferencesManager(LocalContext.current)


    Surface(
        color = Color(0xFF253334),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sign Up",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight(500),
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(42.dp))
            CustomTextField(
                hint = "Full Name",
                value = name,
                onValueChange = { name = it },
                keyboardType = KeyboardType.Text
            )

            CustomTextField(
                hint = "Email Address",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )

            CustomTextField(
                hint = "Password",
                value = password,
                onValueChange = { password = it },
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                SignUpButton(
                    onClick = {
                        if (isEmailValidated(email)){
                            isLoading = true
                            val firebaseAuth = FirebaseAuth.getInstance()
                            try {
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { authTask ->
                                        if (authTask.isSuccessful) {
                                            val user = authTask.result?.user
                                            preferencesManager.saveName("userName",name)
                                            preferencesManager.saveEmail("userEmail",email)
                                            val db = Firebase.firestore
                                            val newUser = hashMapOf(
                                                "name" to name,
                                                "email" to email,
                                                "userId" to user?.uid
                                            )
                                            db.collection("users")
                                                .document(user?.uid ?: "")
                                                .set(newUser)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        navController.context,
                                                        "Sign Up Successful!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    navController.navigate("login") {
                                                        popUpTo("signup") { inclusive = true }
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        navController.context,
                                                        "Error: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    isLoading = false
                                                }
                                        } else {
                                            Toast.makeText(
                                                navController.context,
                                                "Some error occurred!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            isLoading = false
                                        }
                                    }
                            } catch (e: FirebaseAuthException) {
                                when (e.errorCode) {
                                    "auth/email-already-in-use" -> {
                                        Toast.makeText(
                                            navController.context,
                                            "Email already registered!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        isLoading = false
                                    }
                                }
                                Toast.makeText(
                                    navController.context,
                                    "Some error occurred!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Please enter a valid email address",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    },
                    enabled = !isLoading // Disable button while loading
                )
            }
            Row(
                modifier = Modifier.padding(top = 12.dp, bottom = 52.dp)
            ) {
                Text(
                    "Already have an account? ",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.White
                    )
                )

                Text("Sign In",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight(800),
                        color = Color.White
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}




@Composable
fun SignUpButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Primary,
    contentColor: Color = Color.White,
    onClick: () -> Unit,
    enabled: Boolean = true

) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp)) // Add rounded corners
    ) {
        Text(
            text = "Sign Up",
            color = contentColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
fun isEmailValidated(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


