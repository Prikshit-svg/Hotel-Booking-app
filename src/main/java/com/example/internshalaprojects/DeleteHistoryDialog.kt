package com.example.internshalaprojects




import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.example.internshalaprojects.database.HistoryEvent
import com.example.internshalaprojects.database.HistoryState

@Composable
fun DeleteHistoryDialog(
    state : HistoryState,
    onEvent:(HistoryEvent)-> Unit,
    modifier : Modifier= Modifier
){
    AlertDialog(
        onDismissRequest = {
            onEvent(HistoryEvent.HideDialog)
        },
        title = {
            // title expects a composable lambda
            Text(text = "Are you sure you want to delete it from History")
        },
        text = {
            // text also expects a composable lambda
            Text(text = "This action cannot be undone.")
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    state.selectedItem?.let { item ->
                        onEvent(HistoryEvent.DeleteItem(item))
                    }
                    onEvent(HistoryEvent.HideDialog)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    onEvent(HistoryEvent.HideDialog)
                }
            ) {
                Text("Cancel")
            }
        }

    )
}