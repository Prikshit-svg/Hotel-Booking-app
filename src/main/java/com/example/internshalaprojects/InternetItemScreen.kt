package com.example.internshalaprojects


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.internshalaprojects.data.Internetitem

@Composable
fun InternetItemScreen(appViewModel : AppViewModel,
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
                    AsyncImage(model = it.image, contentDescription = it.name, modifier = Modifier.size(128.dp))
                    Text(text = it.name)
                }
            }
        }
        is AppViewModel.ItemUiState.Error ->{
            ErrorScreen(appViewModel)
        }

    }



}

