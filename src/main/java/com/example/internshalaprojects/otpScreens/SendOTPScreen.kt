package com.example.internshalaprojects.otpScreens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
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

// Safe helper to get Activity from any Context
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No activity found")
}

@Composable
fun sendOTPScreen(
    appViewModel: AppViewModel,
    context: Context,
    onClick: () -> Unit,
    auth: FirebaseAuth
) {
    val phoneNumber by appViewModel.phoneNumber.collectAsState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
var error=remember { mutableStateOf("") }
    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-verification on some devices (e.g. SIM-based instant verify)
                appViewModel.setIsLoading(false)
                appViewModel.signInWithPhoneAuthCredential(credential, auth, context)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                appViewModel.setIsLoading(false)
                Toast.makeText(
                    context,
                    "",
                    Toast.LENGTH_LONG
                ).show()
                error.value="Verification failed: ${e.message}"

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                appViewModel.setIsLoading(false)
                appViewModel.setVerificationId(verificationId)
                Toast.makeText(context, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
                onClick() // Navigate to verify screen
            }
        }
    }

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
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = CardDefaults.elevatedShape
        ) {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.padding(13.dp),
                label = { Label(Icons.Outlined.AccountCircle, "Full name") }
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.padding(13.dp),
                label = { Label(Icons.Outlined.Email, "Email") }
            )
            OutlinedTextField(
                value = phoneNumber ?: "",
                onValueChange = { appViewModel.savePhoneNumber(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(13.dp),
                label = { Label(Icons.Outlined.Phone, "Phone number") }
            )

            TextButton(
                onClick = {
                    // Validate phone number
                    if (phoneNumber.isNullOrBlank() || (phoneNumber?.length ?: 0) < 10) {
                        Toast.makeText(
                            context,
                            "Please enter a valid 10-digit phone number",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    appViewModel.setIsLoading(true)

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91${phoneNumber}") // Country code + number
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context.findActivity()) // ✅ Safe Activity fetch
                        .setCallbacks(callbacks)             // ✅ Stable callbacks object
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(options)
                },
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth()
                    .background(Color.Cyan),
            ) {
                Text("Send OTP")
            }
            Text(error.value,modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.padding(170.dp))
        }

    }
}

@Composable
fun Label(icon: ImageVector, text: String) {
    Row(Modifier.fillMaxWidth()) {
        Icon(imageVector = icon, contentDescription = icon.name)
        Spacer(modifier = Modifier.padding(6.dp))
        Text(text)
    }
}