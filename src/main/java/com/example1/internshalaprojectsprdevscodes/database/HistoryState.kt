package com.example1.internshalaprojectsprdevscodes.database

import com.example1.internshalaprojectsprdevscodes.HistoryItem

data class HistoryState(
    val name: String = "",
    val category: String = "",
    val history: List<HistoryItem> = emptyList(),
    val sortType: SortType = SortType.LAST_ADDED,
    val isDeletingItem: Boolean = false,
    val selectedItem: HistoryItem? = null
)
