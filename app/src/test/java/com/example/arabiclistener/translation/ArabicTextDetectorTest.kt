package com.example.arabiclistener.translation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArabicTextDetectorTest {
    @Test
    fun containsArabic_returnsTrue_forArabicText() {
        assertTrue(ArabicTextDetector.containsArabic("مرحبا"))
    }

    @Test
    fun containsArabic_returnsFalse_forEnglishText() {
        assertFalse(ArabicTextDetector.containsArabic("hello world"))
    }

    @Test
    fun containsArabic_returnsTrue_forMixedText() {
        assertTrue(ArabicTextDetector.containsArabic("Price مرحبا 100"))
    }
}
