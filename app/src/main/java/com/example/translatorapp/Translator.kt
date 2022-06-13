package com.example.translatorapp

class Translator ()  {

    companion object {
        fun translate(
            textToTranslate:String,
            targetLanguage: TargetLanguage
        ) : String
        {
            var translated = textToTranslate.reversed()

            return translated
        }
    }
}
