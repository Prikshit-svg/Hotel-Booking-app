package com.example.internshalaprojects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internshalaprojects.database.HistoryEvent
import com.example.internshalaprojects.database.HistoryState
import com.example.internshalaprojects.database.SortType

@Composable
fun HistoryScreen(
    onEvent: (HistoryEvent) -> Unit,
    state: HistoryState
){
    Scaffold() {paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach {
                        Row(
                            modifier = Modifier.clickable{
                                onEvent(HistoryEvent.SortItems(it))
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(state.sortType==it, onClick = {
                                onEvent(HistoryEvent.SortItems(it))
                            })
                            Text(it.name)
                        }
                    }
                }
            }


        }
        LazyRow( contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            items(state.history){
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(it.name, fontSize = 18.sp)
                        Text(it.category, fontSize = 12.sp)
                    }
                    IconButton(
                        onClick = {
                            onEvent(HistoryEvent.ShowDialog(it))
                        }
                    ) {
                        Icon(Icons.Default.Delete,null)
                    }
                }
            }
        }

    }
}