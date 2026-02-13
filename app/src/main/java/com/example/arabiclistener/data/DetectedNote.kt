package com.example.arabiclistener.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detected_notes")
data class DetectedNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceArabic: String,
    val englishTranslation: String,
    val hinglishNote: String,
    val timestampMillis: Long = System.currentTimeMillis()
)
