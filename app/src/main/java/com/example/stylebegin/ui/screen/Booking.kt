package com.example.stylebegin.ui.screen

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stylebegin.R
import com.example.stylebegin.Utils.PUBLISHABLE_KEY
import com.example.stylebegin.ui.PreferencesManager
import com.example.stylebegin.ui.api.ApiUtilities
import com.example.stylebegin.ui.theme.AccentColor
import com.example.stylebegin.ui.theme.Primary
import com.example.stylebegin.ui.theme.PurpleGrey80
import com.example.stylebegin.ui.theme.ShadeWhite
import com.example.stylebegin.ui.theme.WhiteColor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.pay.button.PayButton
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Booking(navController: NavHostController, title: String, price: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AccentColor)
    ) {
        WelcomeMessage()
        ImageBox()
        ServiceMessage(title = title)
        DatePicking(navController, service = title,price=price)


    }
}

@Composable
fun WelcomeMessage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) { // Left side with text
            Text(
                text = "Sharp cuts,",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline
                )
            )
            Text(
                text = "bold looks.",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}

@Composable
fun ImageBox(
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(color = Primary)
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_picture),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

@Composable
fun ServiceMessage(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicking(navHostController: NavHostController, modifier: Modifier = Modifier, service: String,price: String) {

    val context = LocalContext.current
    var currentDate = remember {
        mutableStateOf(
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
    }

    val currentTime =
        remember { mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))) }
    val firestore = FirebaseFirestore.getInstance()

    val preferenceManager = remember {
        PreferencesManager(context)
    }
    var name=preferenceManager.getName("userName","")
    var userEmail=preferenceManager.getName("userEmail","")
    val calendarState = rememberSheetState()
    CalendarDialog(state = calendarState, selection = CalendarSelection.Date { selectedDate ->
        currentDate.value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    })
    val clockState = rememberSheetState()
    ClockDialog(state = clockState, selection = ClockSelection.HoursMinutes { hours, minutes ->
        currentTime.value =
            LocalTime.of(hours, minutes).format(DateTimeFormatter.ofPattern("HH:mm"))
    })

    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = Primary)
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Select your appointment",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    modifier = Modifier
                        .padding(start = 24.dp, top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { calendarState.show() }) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.baseline_date_range_24),
                        contentDescription = "date",
                        modifier = Modifier
                            .padding(4.dp)
                            .width(100.dp)
                            .height(100.dp)
                    )
                }
                IconButton(onClick = { clockState.show() }) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.baseline_access_time_24),
                        contentDescription = "time",
                        modifier = Modifier
                            .padding(4.dp)
                            .width(100.dp)
                            .height(100.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Selected date: ${currentDate.value}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Selected time: ${currentTime.value}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val booking = hashMapOf(
                    "service" to service,
                    "date" to currentDate.value,
                    "time" to currentTime.value,
                    "name" to name,
                    "email" to userEmail
                )
                firestore.collection("booking")
                    .add(booking)
                    .addOnSuccessListener { documentReference ->
                        navHostController.navigate("payment/${price}")
                    }
                    .addOnFailureListener { e ->

                        Toast.makeText(context, "Booking Failed!", Toast.LENGTH_SHORT).show()

                    }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
        )
        { Text(text = "Book", color = Color.White, style = MaterialTheme.typography.bodyLarge) }
    }
}


@Composable
@Preview
fun PreviewOfWelcomeMessage() {
}