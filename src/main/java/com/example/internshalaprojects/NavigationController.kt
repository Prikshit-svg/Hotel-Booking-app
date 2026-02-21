package com.example.internshalaprojects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.internshalaprojects.otpScreens.LoginScreen
import com.example.internshalaprojects.otpScreens.SignUpScreen
import com.example.internshalaprojects.otpScreens.sendOTPScreen
import com.example.internshalaprojects.otpScreens.verifyOTPScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavHostController(appViewModel : AppViewModel){
// Use your app or activity context to instantiate a client instance of
// CredentialManager.

    val auth= FirebaseAuth.getInstance()
    val user by appViewModel.user.collectAsState()
    val navController =rememberNavController()
    val context=LocalContext.current
    val credentialManager = CredentialManager.create(context)
    auth.currentUser?.let {  (appViewModel.setUser(it))}
    val startDestination : String

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
    NavHost(startDestination = "signup", navController = navController){
        composable("signup"){
            appViewModel.setScreenTitle("Sign Up")
            SignUpScreen(auth = auth,
                onNavigateToLogin = {navController.navigate("login")},
                appViewModel = appViewModel,
                onNavigateToHomeScreen = {navController.navigate("home")},
                context=context,
                onNavigateToPhoneAuth = {navController.navigate("sendotpscreen")
        })}
        composable("login") {
            appViewModel.setScreenTitle("Login")
            LoginScreen(
                appViewModel = appViewModel,
                onNavigateToSignUp = { navController.navigate("signup") },
                onSignInSuccess ={navController.navigate("home")},
                context = context,
                auth = auth,
            )
        }
        composable("home"){
            appViewModel.setScreenTitle("Home")
            HomeScreen(appViewModel,{
                navController.navigate("signup")
            },navController,auth,user,context)
        }
        composable("findroomscreen"){
            appViewModel.setScreenTitle("Find Room")
           FindRoomScreen(appViewModel,navController,auth,
               {navController.navigate("signup")})
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
