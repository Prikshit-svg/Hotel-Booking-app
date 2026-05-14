package com.example.internshalaprojects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internshalaprojects.database.HistoryEvent
import com.example.internshalaprojects.database.HistoryState
import com.example.internshalaprojects.database.SortType

@Composable
fun HistoryScreen(
    onEvent: (HistoryEvent) -> Unit,
    state: HistoryState
) {
    if (state.history.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {

        // Section title
        Text(
            text = "Recent Searches",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Sort toggle row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SortType.values().forEach { sortType ->
                Row(
                    modifier = Modifier.clickable {
                        onEvent(HistoryEvent.SortItems(sortType))
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.sortType == sortType,
                        onClick = { onEvent(HistoryEvent.SortItems(sortType)) }
                    )
                    Text(
                        text = sortType.name.replace("_", " "),
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontally scrollable history cards
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.history) { item ->
                Row(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = item.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = item.category,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onEvent(HistoryEvent.ShowDialog(item))
                            
                        }

                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFF2196F3)
                        )
                    }

                }
            }
        }


        Spacer(modifier = Modifier.height(12.dp))
        if(state.isDeletingItem){
            DeleteHistoryDialog(
                state = state,
                onEvent = onEvent
            )
        }
    }
}
