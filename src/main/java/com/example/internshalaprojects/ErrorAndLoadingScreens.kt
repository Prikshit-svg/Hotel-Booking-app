package com.example.internshalaprojects

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants

@Composable
fun ErrorScreen(appViewModel : AppViewModel){
    Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Image(painterResource(R.drawable.errorscreen),
            contentDescription = "Connection Error",
            modifier=Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop)
        Button(onClick = { appViewModel.getHotelItems() }) {
            Text(text = "Retry", fontSize = 20.sp)
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorScreenPreview(){
    //ErrorScreen()
}