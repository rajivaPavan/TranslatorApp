package com.example.translatorapp

sealed class TargetLanguage(val name: String){
    object Reversed : TargetLanguage("Reversed")
    object ReversedWords : TargetLanguage("Reversed words")
    object Acronyms: TargetLanguage("Acronyms")
    object Symbols : TargetLanguage("Symbols")
    object ChangingCase : TargetLanguage("Changing case")
    object Highlighted : TargetLanguage("Highlighted")
}

class Translator ()  {



    companion object {
        fun translate(
            textToTranslate:String,
            targetLanguage: TargetLanguage
        ) : String
        {
            var translated = textToTranslate.reversed()

//            when(language){
//
//            }

            return translated
        }
    }
}
