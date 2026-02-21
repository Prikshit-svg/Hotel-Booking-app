package com.example.internshalaprojects.otpScreens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.internshalaprojects.AppViewModel

import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(
    onNavigateToLogin : () -> Unit,
    appViewModel : AppViewModel,
    onNavigateToHomeScreen : () -> Unit,
    auth : FirebaseAuth,
    context : Context,
    onNavigateToPhoneAuth : () -> Unit
) {
    val email= remember{ mutableStateOf("") }
    val password= remember{ mutableStateOf("") }
    val firstName= remember{ mutableStateOf("") }
    val lastName= remember{ mutableStateOf("") }
    val confirmPassword= remember{ mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(value = email.value, onValueChange = {email.value=it},
            label = {Text("Email")}, modifier = Modifier.padding(10.dp).fillMaxWidth())

        OutlinedTextField(value = password.value, onValueChange = {password.value=it},
            label = {Text("Password")}, modifier = Modifier.padding(10.dp).fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation())

        OutlinedTextField(value = firstName.value, onValueChange = {firstName.value=it},
            label = {Text("First Name")}, modifier = Modifier.padding(10.dp).fillMaxWidth())

        OutlinedTextField(value = lastName.value, onValueChange = {lastName.value=it},
            label = {Text("Last Name")}, modifier = Modifier.padding(10.dp).fillMaxWidth())

        OutlinedTextField(value = confirmPassword.value, onValueChange = {confirmPassword.value=it},
            label = {Text("Confirm Password")}, modifier = Modifier.padding(10.dp).fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation())


        Button(
            onClick = {
            //add the signup function
            appViewModel.signUp(auth,context,email.value,password.value)
            email.value = ""
            password.value = ""
            firstName.value = ""
            lastName.value = ""
            confirmPassword.value=""
                onNavigateToHomeScreen()

        }) {
            Text("Sign up")
        }
        GoogleSignInButton(appViewModel = appViewModel, auth = auth)
        Text("Already have an account? Log in.",
            modifier = Modifier.clickable { //add navigation
                onNavigateToLogin()
                })
        Text("Sign up using phone number",
            Modifier.clickable {
              onNavigateToPhoneAuth()

            })

    }
    }


@Preview(showBackground = true)
@Composable
fun SignupPreview() {
   // SignUpScreen()
}
@Composable
fun LoginScreen(onNavigateToSignUp : () -> Unit,
                appViewModel : AppViewModel,
                onSignInSuccess:()->Unit,
                context: Context,
                auth: FirebaseAuth
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val result = appViewModel.authResult.value

    Column(
        Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(

            onClick = {
                //add the login function
                appViewModel.logIn(auth, context,email.value,password.value)
                email.value = ""
                password.value = ""
                onSignInSuccess()
            },

        ) {
            Text("Login")
        }
        Text("don't have an account? Sign up.",
            modifier = Modifier.clickable {
                //add navigation
                onNavigateToSignUp()
            })
    }
}