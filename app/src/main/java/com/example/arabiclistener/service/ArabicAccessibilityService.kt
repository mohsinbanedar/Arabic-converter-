package com.example.arabiclistener.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.example.arabiclistener.data.DetectedNote
import com.example.arabiclistener.data.NoteDatabase
import com.example.arabiclistener.data.NoteRepository
import com.example.arabiclistener.translation.ArabicTextDetector
import com.example.arabiclistener.translation.ArabicTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ArabicAccessibilityService : AccessibilityService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var repository: NoteRepository
    private val translator = ArabicTranslator()

    @Volatile
    private var lastCapturedText: String = ""

    override fun onServiceConnected() {
        super.onServiceConnected()
        repository = NoteRepository(NoteDatabase.getInstance(applicationContext).noteDao())
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val text = event?.text?.joinToString(" ")?.trim().orEmpty()
        if (text.isBlank() || !ArabicTextDetector.containsArabic(text)) return
        if (text == lastCapturedText) return
        lastCapturedText = text

        serviceScope.launch {
            val quickEnglish = translator.quickEnglish(text)
            val quickHinglish = translator.quickHinglish(text)

            val id = repository.addNote(
                DetectedNote(
                    sourceArabic = text,
                    englishTranslation = quickEnglish,
                    hinglishNote = quickHinglish
                )
            )

            val improvedEnglish = translator.improveEnglishWithMlKit(text) ?: return@launch
            if (improvedEnglish.equals(quickEnglish, ignoreCase = true)) return@launch

            repository.updateNote(
                DetectedNote(
                    id = id,
                    sourceArabic = text,
                    englishTranslation = improvedEnglish,
                    hinglishNote = translator.englishToHinglish(improvedEnglish)
                )
            )
        }
    }

    override fun onInterrupt() = Unit

    override fun onDestroy() {
        super.onDestroy()
        translator.close()
        serviceScope.cancel()
    }
}
