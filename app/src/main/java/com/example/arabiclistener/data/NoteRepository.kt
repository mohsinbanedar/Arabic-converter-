package com.example.arabiclistener.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {
    fun observeNotes(): Flow<List<DetectedNote>> = dao.observeAll()

    suspend fun addNote(note: DetectedNote): Long {
        return dao.insert(note)
    }

    suspend fun updateNote(note: DetectedNote) {
        dao.update(note)
    }
}
