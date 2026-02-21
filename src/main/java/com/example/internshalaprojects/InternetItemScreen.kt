package com.example.internshalaprojects


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun InternetItemScreen(
    appViewModel : AppViewModel,
    itemUiState : AppViewModel.ItemUiState,
){
    when(itemUiState){
        is AppViewModel.ItemUiState.Loading ->{CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )   }
        is AppViewModel.ItemUiState.Success ->{
            TextButton(onClick = {

            }) {
                Text("visit official website")
            }
            LazyRow(Modifier.fillMaxWidth()) {
                items(itemUiState.items){
                    Column(modifier = Modifier.fillMaxWidth().width(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        AsyncImage(model = it.image, contentDescription = null,modifier=Modifier
                            .size(150.dp)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop)
                        Text(it.name, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }

                }
            }
        }
        is AppViewModel.ItemUiState.Error ->{
            ErrorScreen(appViewModel)
        }

    }



}

