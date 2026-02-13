package com.example.arabiclistener.translation

object ArabicTextDetector {
    fun containsArabic(input: String): Boolean {
        return input.any { ch ->
            ch in '\u0600'..'\u06FF' ||
                ch in '\u0750'..'\u077F' ||
                ch in '\u08A0'..'\u08FF' ||
                ch in '\uFB50'..'\uFDFF' ||
                ch in '\uFE70'..'\uFEFF'
        }
    }
}
