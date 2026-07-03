package com.example1.internshalaprojectsprdevscodes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class HistoryItem(

    val name:String,
    val category:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0
)

@Dao
interface HistoryItemDao{
    @Insert
    suspend fun insertItem(item:HistoryItem)
    @Delete
    suspend fun deleteItem(item:HistoryItem)
    @Query("SELECT * FROM historyitem ORDER BY id DESC")
    fun getAllItemsAccordingToLastAdded(): Flow<List<HistoryItem>>

    @Query("SELECT * FROM historyitem ORDER BY id ASC")
     fun getAllItemsAccordingToFirstAdded(): Flow<List<HistoryItem>>



}