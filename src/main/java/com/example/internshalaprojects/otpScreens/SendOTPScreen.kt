package com.example.internshalaprojects.otpScreens

import android.app.Activity // Import Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internshalaprojects.AppViewModel
import com.example.internshalaprojects.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun sendOTPScreen(
    appViewModel: AppViewModel,
    context: Context, // Keep context for Toasts
    onClick: () -> Unit,
    auth: FirebaseAuth
) {
    val phoneNumber by appViewModel.phoneNumber.collectAsState()

    // 1. DEFINE a single, reusable callbacks object.
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This is called automatically on some devices.
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle failure, show a toast, etc.
            appViewModel.setIsLoading(false)
            Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // Code was sent successfully. Save the verificationId and navigate.
            Toast.makeText(context, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
            appViewModel.setIsLoading(false)
            appViewModel.setVerificationId(verificationId)
            onClick() // Navigate to the verify screen
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_blue_500)),
    ) {
        Spacer(Modifier.padding(60.dp))
        Text("Sign up", color = MaterialTheme.colorScheme.primary, fontSize = 32.sp, modifier = Modifier.padding(10.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = CardDefaults.elevatedShape
        ) {
            OutlinedTextField(value = "", onValueChange = {}, Modifier.padding(13.dp), label = { label(Icons.Outlined.AccountCircle, "full name") })
            OutlinedTextField(value = "", onValueChange = {}, Modifier.padding(13.dp), label = { label(Icons.Outlined.Email, "Email") })
            OutlinedTextField(
                value = phoneNumber ?: "",
                onValueChange = { appViewModel.savePhoneNumber(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(13.dp),
                label = { label(Icons.Outlined.Phone, "Phone number") }
            )

            TextButton(
                onClick = {
                    // 2. The onClick is now clean and has a single responsibility.
                    if (phoneNumber.isNullOrBlank() || (phoneNumber?.length ?: 0) < 10) {
                        Toast.makeText(context, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    appViewModel.setIsLoading(true)

                    // 3. Build the options and start verification.
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91$phoneNumber") // Use the validated phone number
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context as Activity) // This cast is still needed here
                        .setCallbacks(callbacks) // Use the single callbacks object
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                    // We navigate away inside onCodeSent now, so we can remove onClick() from here
                },
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth()
                    .background(Color.Cyan),
            ) {
                Text("Send OTP")
            }
            Spacer(Modifier.padding(170.dp))
        }
    }
}

// Your label and preview functions are fine.
@Composable
fun label(icon: ImageVector, text: String) {
    Row(Modifier.fillMaxWidth()) {
        Icon(imageVector = icon, "${icon.name}")
        Spacer(modifier = Modifier.padding(6.dp))
        Text(text)
    }
}

@Preview
@Composable
fun sendOTPScreenPreview() {
    //sendOTPScreen()
}
