package com.example.arabiclistener.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: DetectedNote): Long

    @Update
    suspend fun update(note: DetectedNote)

    @Query("SELECT * FROM detected_notes ORDER BY timestampMillis DESC")
    fun observeAll(): Flow<List<DetectedNote>>
}
