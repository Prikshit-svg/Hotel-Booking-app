package com.example.internshalaprojects.database

import com.example.internshalaprojects.HistoryItem

sealed interface HistoryEvent {
    data class SaveItem(val name: String, val category: String) : HistoryEvent
    data class ShowDialog(val item: HistoryItem): HistoryEvent
    object HideDialog: HistoryEvent
    data class SortItems(val sortType: SortType): HistoryEvent
    data class DeleteItem(val item: HistoryItem): HistoryEvent

    data class SetName(val name: String): HistoryEvent
    data class SetCategory(val category: String): HistoryEvent

}
enum class SortType {
    FIRST_ADDED,
    LAST_ADDED
}