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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stylebegin.ui.components.CustomTextField
import com.example.stylebegin.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminLogin(navHostController: NavHostController) {
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                text = "Sign In As Admin",
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
                hint = "Username",
                value = username,
                onValueChange = { username = it },
                keyboardType = KeyboardType.Text,
                isPassword = false
            )

            CustomTextField(
                hint = "Password",
                value = password,
                onValueChange = { password = it },
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(26.dp))
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                AdminSignInButton(
                    onClick = {
                        isLoading = true

                        val firestore = FirebaseFirestore.getInstance()
                        val adminRef = firestore.collection("admin")

                        adminRef.get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    val document = documents.documents[0]
                                    val adminUsername = document.getString("id")
                                    val adminPassword = document.getString("password")

                                    if (username == adminUsername && password == adminPassword) {

                                        Toast.makeText(
                                            navHostController.context,
                                            "Sign In Successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navHostController.navigate("adminPage") {
                                            popUpTo(0)
                                        }
                                    } else {
                                        // Incorrect username or password
                                        Toast.makeText(
                                            navHostController.context,
                                            "Incorrect username or password",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        isLoading = false
                                    }
                                } else {
                                    // No admin document found
                                    Toast.makeText(
                                        navHostController.context,
                                        "Admin document not found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    isLoading = false
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle error
                                Toast.makeText(
                                    navHostController.context,
                                    "Error: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isLoading = false
                            }
                    },
                    enabled = !isLoading // Disable button while loading
                )
            }

        }

    }
}
@Composable
fun AdminSignInButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Primary, // Change to your desired color
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
        Text(text = "Sign In", color = contentColor, style = MaterialTheme.typography.bodyLarge)
    }
}