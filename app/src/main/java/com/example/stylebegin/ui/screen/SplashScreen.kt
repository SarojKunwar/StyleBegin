package com.example.stylebegin.ui.screen

import android.window.SplashScreen
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stylebegin.R
import com.example.stylebegin.ui.theme.AccentColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var timerValue by remember { mutableStateOf(3f) } // Timer in seconds

    val animateAlpha = animateFloatAsState(
        targetValue = if (timerValue > 0) 1f else 0f,
        animationSpec = TweenSpec(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        while (timerValue > 0) {
            delay(1000L)
            timerValue -= 1f
            if (timerValue == 0f) {
                navController.navigate("login"){
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AccentColor), // Remove background
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen), // Replace with your image resource ID
            contentDescription = "Welcome Image",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(600  .dp)
                .alpha(animateAlpha.value) // Apply alpha animation
        )
    }
}