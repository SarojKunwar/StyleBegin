package com.example.stylebegin.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stylebegin.ui.theme.Purple80
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun fetchBookings(): List<Map<String, Any>> {

    val db = FirebaseFirestore.getInstance()
    val snapshot = db.collection("booking").get().await()
    val bookings = mutableListOf<Map<String, Any>>()
    for (doc in snapshot.documents) {
        bookings.add(doc.data!!)
    }

    return bookings
}

@Composable
fun BookingCard(booking: Map<String, Any>) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var email= booking["email"]
    val confirmMessage="Your booking is confirmed at ${booking["date"]} at ${booking["time"]}"
    Card(
        colors = CardDefaults.cardColors(containerColor = Purple80),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Service: ${booking["service"]}", style = TextStyle(fontSize = 18.sp))
            Text(text = "Name: ${booking["name"]}", style = TextStyle(fontSize = 18.sp))
            Text(text = "Date: ${booking["date"]}", style = TextStyle(fontSize = 18.sp))
            Text(text = "Time: ${booking["time"]}", style = TextStyle(fontSize = 18.sp))
            Text(
                text = "Confirm",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Blue
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        openEmailer(email,confirmMessage,context)
                    }
            )
        }
    }
}

fun openEmailer(email: Any?, confirmMessage: String, context: Context) {
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.type = "message/rfc822"
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.toString()))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Booking Confirmation")
    emailIntent.putExtra(Intent.EXTRA_TEXT, confirmMessage)
    emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    val chooserIntent = Intent.createChooser(emailIntent, "Send Email")
    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(chooserIntent)
}

@Composable
fun AdminPage(navController: NavController) {
    // Placeholder for bookings data
    var bookings by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(true) {
        val fetchedBookings = fetchBookings()
        bookings = fetchedBookings
    }

    // Display bookings in a LazyColumn
    Column() {
        BookingMessage(title = "Our Bookings")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            items(bookings) { booking ->
                BookingCard(booking = booking)
            }
        }
    }

}
@Composable
fun BookingMessage(title: String) {
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
                modifier = Modifier.align(Alignment.Center) // Center both horizontally and vertically
            )
        }
    }
}