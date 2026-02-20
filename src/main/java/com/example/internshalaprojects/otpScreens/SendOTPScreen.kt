package com.example.internshalaprojects.otpScreens

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.internshalaprojects.AppViewModel
import com.example.internshalaprojects.R

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthOptions.newBuilder
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun sendOTPScreen(appViewModel : AppViewModel,context: Context,onClick:()->Unit,auth: FirebaseAuth){
    val phoneNumber by appViewModel.phoneNumber.collectAsState()
    val verificationId by appViewModel.verificationId.collectAsState()

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
            appViewModel.setVerificationId(verificationId)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_blue_500)),
    ){

        Spacer(Modifier.padding(60.dp))
        Text("Sign up", color = MaterialTheme.colorScheme.primary, fontSize = 32.sp, modifier = Modifier.padding(10.dp))
        Card(Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Sets the background color of the card
            ),
            shape = CardDefaults.elevatedShape) {
            OutlinedTextField(value = "", onValueChange ={}, Modifier.padding(13.dp), label = {label(Icons.Outlined.AccountCircle,"full name")} )
            OutlinedTextField(value = "", onValueChange ={}, Modifier.padding(13.dp) ,label = {label(Icons.Outlined.Email,"Email")})

            OutlinedTextField(value =phoneNumber?:"",
                onValueChange ={appViewModel.savePhoneNumber(it)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
               modifier = Modifier.padding(13.dp) ,label = {label(Icons.Outlined.Phone,"Phone number")})
            TextButton(
                onClick = {
                    val options = newBuilder(auth)
                        .setPhoneNumber("91$phoneNumber") // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(context as Activity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                    onClick()

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

@Composable
fun label(icon: ImageVector, text : String){
    Row(Modifier.fillMaxWidth()) {
        Icon(imageVector = icon,"${icon.name}")
        Spacer(modifier = Modifier.padding(6.dp))
        Text(text)
    }
}

@Preview
@Composable
fun sendOTPScreenPreview(){
    //sendOTPScreen()
}

