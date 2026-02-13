package com.example.arabiclistener.translation

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

class ArabicTranslator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ARABIC)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()

    private val translator: Translator = Translation.getClient(options)

    private val phraseDictionary = linkedMapOf(
        "كيف حالك" to "how are you",
        "شكرا" to "thank you",
        "شكرا لك" to "thank you",
        "مرحبا" to "hello",
        "صباح الخير" to "good morning",
        "مساء الخير" to "good evening",
        "نعم" to "yes",
        "لا" to "no",
        "مع السلامة" to "goodbye"
    )

    private val wordDictionary = mapOf(
        "انا" to "I",
        "أنت" to "you",
        "انت" to "you",
        "هو" to "he",
        "هي" to "she",
        "نحن" to "we",
        "السلام" to "peace",
        "الله" to "allah",
        "جيد" to "good",
        "اليوم" to "today",
        "ماء" to "water",
        "بيت" to "house",
        "كتاب" to "book"
    )

    private val transliterationMap = mapOf(
        'ا' to "a", 'أ' to "a", 'إ' to "i", 'آ' to "aa", 'ب' to "b", 'ت' to "t", 'ث' to "th",
        'ج' to "j", 'ح' to "h", 'خ' to "kh", 'د' to "d", 'ذ' to "z", 'ر' to "r", 'ز' to "z",
        'س' to "s", 'ش' to "sh", 'ص' to "s", 'ض' to "z", 'ط' to "t", 'ظ' to "z", 'ع' to "a",
        'غ' to "gh", 'ف' to "f", 'ق' to "q", 'ك' to "k", 'ل' to "l", 'م' to "m", 'ن' to "n",
        'ه' to "h", 'ة' to "a", 'و' to "w", 'ي' to "y", 'ى' to "a", 'ء' to "'"
    )

    fun quickEnglish(arabic: String): String {
        val normalized = arabic.trim()
        phraseDictionary[normalized]?.let { return it }

        val words = normalized.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (words.isEmpty()) return normalized

        val translated = words.map { word ->
            val clean = word.trim('،', ',', '.', '!', '?', '؟', ';', ':')
            wordDictionary[clean] ?: transliterateArabic(clean)
        }

        return translated.joinToString(" ")
    }

    fun quickHinglish(arabic: String): String {
        return quickEnglish(arabic)
    }

    suspend fun improveEnglishWithMlKit(arabic: String): String? {
        return try {
            translator.downloadModelIfNeeded().await()
            translator.translate(arabic).await()
        } catch (_: Exception) {
            null
        }
    }

    fun englishToHinglish(english: String): String {
        return english.trim()
    }

    private fun transliterateArabic(word: String): String {
        if (word.isBlank()) return word
        val base = buildString {
            word.forEach { ch ->
                append(transliterationMap[ch] ?: ch)
            }
        }
        return base.replace(Regex("(.)\\1+"), "$1")
    }

    fun close() {
        translator.close()
    }
}
