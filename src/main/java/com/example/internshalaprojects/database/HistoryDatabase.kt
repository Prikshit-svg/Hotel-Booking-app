package com.example.internshalaprojects.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.internshalaprojects.HistoryItem
import com.example.internshalaprojects.HistoryItemDao

@Database(
    entities=[HistoryItem::class],
    version=1
)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val dao: HistoryItemDao
}


