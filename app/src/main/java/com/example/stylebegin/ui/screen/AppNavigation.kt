package com.example.stylebegin.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
//        composable("screen3") { Screen3(navController) }
        composable("booking/{title}/{price}") { backStackEntry ->
            val title= backStackEntry.arguments?.getString("title")
            val price= backStackEntry.arguments?.getString("price")
            if (title!= null && price!= null) {
                Booking(navController,title, price)
            }
        }
        composable("login"){ Login(navController)}
        composable("signup"){ SignUpScreen(navController) }
        composable("adminLogin"){ AdminLogin(navController) }
        composable("adminPage"){ AdminPage(navController) }
        composable("userEdit"){ ProfileEditScreen(navController)}
        composable("payment/{price}"){backStackEntry ->
            val price= backStackEntry.arguments?.getString("price")
            if (price!= null) {
                PaymentPage(price)
            }
             }
    }
}