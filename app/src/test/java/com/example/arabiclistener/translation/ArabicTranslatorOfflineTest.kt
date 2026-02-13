package com.example.arabiclistener.translation

import org.junit.Assert.assertTrue
import org.junit.Test

class ArabicTranslatorOfflineTest {
    private val translator = ArabicTranslator()

    @Test
    fun quickEnglish_translatesKnownPhraseOffline() {
        val result = translator.quickEnglish("شكرا لك")
        assertTrue(result.contains("thank you", ignoreCase = true))
    }

    @Test
    fun quickHinglish_returnsOnlyConvertedContent() {
        val result = translator.quickHinglish("مرحبا")
        assertTrue(result.equals("hello", ignoreCase = true))
    }

    @Test
    fun quickEnglish_transliteratesUnknownWordOffline() {
        val result = translator.quickEnglish("مدرسة")
        assertTrue(result.isNotBlank())
    }
}
