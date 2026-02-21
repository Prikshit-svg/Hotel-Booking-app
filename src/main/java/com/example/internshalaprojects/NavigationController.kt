package com.example.internshalaprojects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.internshalaprojects.otpScreens.sendOTPScreen
import com.example.internshalaprojects.otpScreens.verifyOTPScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavHostController(appViewModel : AppViewModel){
    val auth= FirebaseAuth.getInstance()
    val user by appViewModel.user.collectAsState()
    val navController =rememberNavController()
    val context=LocalContext.current
    auth.currentUser?.let {  (appViewModel.setUser(it))}
    LaunchedEffect(user) {
        if (user != null) {
            // If the user logs in successfully, navigate to the home screen.
            // We also clear the back stack so the user can't go back to the OTP screens.
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }
    NavHost(startDestination = "home", navController = navController){
        composable("home"){
            appViewModel.setScreenTitle("Home")
            HomeScreen(appViewModel,{
                //navController.navigate("internetetitemscreen")
            },navController,auth,user,context)
        }
        composable("findroomscreen"){
            appViewModel.setScreenTitle("Find Room")
           FindRoomScreen(appViewModel,navController)
        }
        composable("sendotpscreen"){
            appViewModel.setScreenTitle("Sign Up")
            sendOTPScreen(appViewModel,context,
                onClick={navController.navigate("verifyotpscreen")},auth)

        }
        composable("verifyotpscreen"){
            appViewModel.setScreenTitle("Verify OTP")
            verifyOTPScreen(appViewModel,auth)

        }
    }
}
