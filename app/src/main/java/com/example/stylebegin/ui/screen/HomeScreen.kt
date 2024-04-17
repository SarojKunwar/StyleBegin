package com.example.stylebegin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stylebegin.R
import com.example.stylebegin.ui.theme.AccentColor
import com.example.stylebegin.ui.theme.Purple80
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(navController: NavController){
    var username= remember {
        mutableStateOf("")
    }
    val services = listOf(
        ServiceItem("Kid HairCutting","$10", R.drawable.service_one),
        ServiceItem("Basic Facial","$10", R.drawable.service_two),
        ServiceItem("Beard Trimming","$10", R.drawable.service_three),
        ServiceItem("Hair Cutting","$10", R.drawable.service_four),
        ServiceItem("Hair Washing","$10", R.drawable.service_five),
        ServiceItem("Clean Shaving","$10", R.drawable.service_six),

    )
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = AccentColor)) {
        TopBar(username.value,navController)
        LineBar()
        ServicesSection(services,navController)
    }
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser

    if (user != null) {
        val userRef = db.collection("users").document(user.uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("name")
                    if (name != null) {
                        username.value=name
                    } else {

                    }
                } else {

                }
            }
            .addOnFailureListener { e ->
                // Handle failures
                // For example, display an error message
            }
    }
}
@Composable
fun TopBar(name: String, navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) { // Left side with text
            Text(text = "Hello,", color = Color.White, style = MaterialTheme.typography.labelSmall)
            Text(text =name ,color= Color.White, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id= R.drawable.scissor),
            contentDescription = "Profile picture of $name",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
                .clickable{
                    navController.navigate("userEdit")
                }
        )

    }

}
@Composable
fun LineBar(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 0.dp)
        .height(4.dp)
        .background(Color.White, RoundedCornerShape(2.dp)))
}
@Composable
fun ServicesSection(services: List<ServiceItem>, navController: NavController) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(
            text = "Our Services",
            color = Color.White,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(services.size) { index ->
                val service = services[index]
                ServiceGridItem(service.title,service.price, service.imageResourceId,navController){
                    navController.navigate("booking/${service.title}/${service.price}")
                }
            }
        }
    }
}

data class ServiceItem(val title: String,val price:String, val imageResourceId: Int)

@Composable
fun ServiceGridItem(title: String,price: String, imageResourceId: Int, navController: NavController, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(color = Purple80)
        .clickable { onClick() }
        ) { // Adjust size modifier here
        Column(modifier = Modifier.padding(4.dp)) {
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.CenterHorizontally) // Fixed image size (adjust as needed)
                // Fill remaining space within the column
            )
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = price,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Composable
@Preview
fun DefaultPreviewOfLineBar(){

}