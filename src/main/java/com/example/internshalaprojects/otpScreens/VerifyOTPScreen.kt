package com.example.internshalaprojects.otpScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.internshalaprojects.AppViewModel
import com.example.internshalaprojects.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions.newBuilder
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun verifyOTPScreen(
    appViewModel: AppViewModel,
    auth: FirebaseAuth
) {
    val timer by appViewModel.timer.collectAsState()
    val context = LocalContext.current
    val otp by appViewModel.otp.collectAsState()
    val verificationId by appViewModel.verificationId.collectAsState()
    val loading by appViewModel.isLoading.collectAsState()
    val phoneNumber by appViewModel.phoneNumber.collectAsState()
    val logout by appViewModel.logout.collectAsState()

    // Reusable resend callbacks wrapped in remember so they
    // don't get recreated on every recomposition
    val resendCallbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-verification — sign in directly
                appViewModel.setIsLoading(false)
                appViewModel.signInWithPhoneAuthCredential(credential, auth, context.findActivity())
            }

            override fun onVerificationFailed(e: FirebaseException) {
                appViewModel.setIsLoading(false)
                Toast.makeText(
                    context,
                    "Resend failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                appViewModel.setIsLoading(false)
                appViewModel.setVerificationId(verificationId)
                appViewModel.runTimer()
                Toast.makeText(context, "OTP Resent Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        appViewModel.setIsLoading(false)
        appViewModel.runTimer()
    }

    Box {
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.light_blue_500)),
        ) {
            Spacer(Modifier.padding(60.dp))
            Text(
                "Sign up",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                modifier = Modifier.padding(10.dp)
            )
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .size(300.dp, 650.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = CardDefaults.elevatedShape,
            ) {
                OtpInput(onOTPComplete = { appViewModel.saveOtp(it) })

                Text(
                    "Verify OTP",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                // Verify OTP button
                TextButton(
                    onClick = {
                        if (verificationId.isBlank()) {
                            Toast.makeText(
                                context,
                                "Could not verify OTP. Please try again.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@TextButton
                        }
                        if (otp.isEmpty()) {
                            Toast.makeText(context, "Enter OTP", Toast.LENGTH_SHORT).show()
                        } else {
                            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                            appViewModel.signInWithPhoneAuthCredential(
                                credential,
                                auth,
                                context.findActivity() // ✅ safe Activity fetch
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                        .fillMaxWidth()
                        .background(Color.Cyan)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text("Verify OTP")
                }

                Spacer(Modifier.padding(10.dp))

                // Resend OTP button
                TextButton(
                    onClick = {
                        if (timer == 0) {
                            if (phoneNumber.isNullOrBlank()) {
                                Toast.makeText(
                                    context,
                                    "Phone number not found. Please go back and try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@TextButton
                            }

                            appViewModel.setIsLoading(true)

                            val options = newBuilder(auth)
                                .setPhoneNumber("+91${phoneNumber}") // ✅ added + prefix
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(context.findActivity()) // ✅ safe Activity fetch
                                .setCallbacks(resendCallbacks)       // ✅ stable callbacks
                                .build()

                            PhoneAuthProvider.verifyPhoneNumber(options)

                        } else {
                            Toast.makeText(
                                context,
                                "Wait for $timer seconds",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                        .fillMaxWidth()
                        .background(Color.Cyan)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = if (timer > 0) "Resend OTP  00:$timer"
                        else "Resend OTP"
                    )
                }
            }
        }

        // Back button
        IconButton(onClick = {
            // navController.popBackStack()
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        // Logout button
        IconButton(
            onClick = { appViewModel.setLogout(true) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Image(painterResource(R.drawable.logout), contentDescription = "logout")
        }

        // Loading overlay
        if (loading) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color(255, 255, 255, 50)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        // Logout dialog
        if (logout) {
            AlertDialogBox(
                noButton = { appViewModel.setLogout(false) },
                yesButton = {
                    auth.signOut()
                    //appViewModel.clearUser()
                    appViewModel.setLogout(false)
                }
            )
        }
    }
}

@Composable
fun OtpInput(
    otpLength: Int = 6,
    onOTPComplete: (String) -> Unit
) {
    val focusRequester = remember { List(otpLength) { FocusRequester() } }
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }

    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        for (i in 0 until otpLength) {
            OutlinedTextField(
                value = otpValues[i],
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.isDigitsOnly()) {
                        otpValues[i] = newValue
                        if (newValue.isNotEmpty() && i < otpLength - 1) {
                            focusRequester[i + 1].requestFocus()
                        }
                        if (otpValues.all { it.isNotEmpty() }) {
                            onOTPComplete(otpValues.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .focusRequester(focusRequester[i])
                    .width(50.dp)
                    .height(50.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester[0].requestFocus()
    }
}

@Composable
fun AlertDialogBox(noButton: () -> Unit, yesButton: () -> Unit) {
    AlertDialog(
        title = { Text("Logout?") },
        containerColor = Color.White,
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = { yesButton() }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { noButton() }) {
                Text("No")
            }
        },
        onDismissRequest = { noButton() }
    )
}