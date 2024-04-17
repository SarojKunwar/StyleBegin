package com.example.stylebegin.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.stylebegin.ui.PreferencesManager
import com.example.stylebegin.ui.api.ApiUtilities

import com.example.stylebegin.ui.components.CustomTextField
import com.example.stylebegin.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Login(navHostController: NavHostController){
    var password by remember { mutableStateOf("") }
    var isValid by remember {
        mutableStateOf(false)
    }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val preferencesManager = PreferencesManager(LocalContext.current)
    var customerId by remember { mutableStateOf("") }
    val apiInterface = ApiUtilities.getApiInterface()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
            Text(text = "Sign In",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight(500),
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(42.dp))
            CustomTextField(hint = "Email Address", value = email, onValueChange = {email=it}, keyboardType = KeyboardType.Email , isPassword = false)

            CustomTextField(hint = "Password", value = password, onValueChange = {password=it}, keyboardType = KeyboardType.Password , isPassword = true)

            Spacer(modifier = Modifier.height(26.dp))
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                SignInButton(
                    onClick = {
                        if (isEmailValid(email)) {
                            isLoading = true
                            val firebaseAuth = FirebaseAuth.getInstance()
                            try {
                                firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener{
                                        if(it.isSuccessful){
                                            navHostController.navigate("home") {
                                                popUpTo("signin") { inclusive = true }
                                            }
                                        }else{
                                            Toast.makeText(navHostController.context, "Some error occured!", Toast.LENGTH_SHORT).show()
                                            isLoading = false
                                        }
                                    }
                            } catch (e: FirebaseAuthException) {
                                when (e.errorCode) {
                                    "auth/user-not-found" -> {
                                        isLoading = false
                                    }
                                    "auth/wrong-password" -> {

                                        isLoading = false
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(navHostController.context, "Please enter a valid email address!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading // Disable button while loading
                )
            }
            Row(
                modifier = Modifier.padding(top=12.dp, bottom = 180.dp)
            ){
                Text("Don't have an account? ",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.White
                    )
                )

                Text("Sign Up",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight(800),
                        color = Color.White
                    ),
                    modifier = Modifier.clickable {
                        navHostController.navigate("signup")
                    }
                )
            }
            Row(){
                Text(
                    "Admin?",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight(800),
                        color = Color.White
                    ),
                    modifier = Modifier.clickable {
                        navHostController.navigate("adminlogin")
                    }
                )
            }
        }

    }
}
@Composable
fun SignInButton(
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

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

