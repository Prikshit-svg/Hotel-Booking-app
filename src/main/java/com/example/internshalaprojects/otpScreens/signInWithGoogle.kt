package com.example.internshalaprojects.otpScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.internshalaprojects.AppViewModel
import com.example.internshalaprojects.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GoogleSignInButton(
    appViewModel: AppViewModel,
    auth: FirebaseAuth
) {
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }

    OutlinedButton(
        onClick = {
            appViewModel.signInWithGoogle(
                context = context,
                auth = auth,
                credentialManager = credentialManager
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.google_logo), // add google logo to drawable
            contentDescription = "Google logo",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sign in with Google")
    }
}