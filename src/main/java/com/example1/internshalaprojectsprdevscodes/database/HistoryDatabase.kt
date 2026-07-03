package com.example1.internshalaprojectsprdevscodes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example1.internshalaprojectsprdevscodes.HistoryItem
import com.example1.internshalaprojectsprdevscodes.HistoryItemDao

@Database(
    entities=[HistoryItem::class],
    version=1
)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val dao: HistoryItemDao
}


