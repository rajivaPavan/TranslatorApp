package com.example.translatorapp

import com.example.translatorapp.textforms.getBoldCharacter
import com.example.translatorapp.textforms.replaceWordIfAcronym
import kotlin.math.ceil

sealed class TargetLanguage(val name: String){
    object Reversed : TargetLanguage("Reversed")
    object ReversedWords : TargetLanguage("Reversed words")
    object Acronyms: TargetLanguage("Acronyms")
    object Symbols : TargetLanguage("Symbols")
    object ChangingCase : TargetLanguage("Changing case")
    object Highlighted : TargetLanguage("Highlighted")
}

class Translator{
    companion object {
        private fun reversed(text:String):String{
            return text.reversed()
        }
        private fun reversedWords(text:String):String{
            val words = text.split(" ")
            val reversedWords = mutableListOf<String>()
            words.forEach {
                reversedWords += it.reversed()
            }
            return reversedWords.joinToString(separator = " ")
        }
        private fun acronyms(text: String):String{
            val words = text.split(" ")
            val res = mutableListOf<String>()
            words.forEach{
                res.add(replaceWordIfAcronym(it))
            }
            return res.joinToString(" ")
        }
        private fun symbols(text: String):String{
            TODO("Not yet implemented")
        }

        private fun highlighted(text: String): String {
            val words = text.split(" ")
            val bionicWords = mutableListOf<String>()
            words.forEach { word ->
                val wordLength = word.length
                if(wordLength>0){
                    var bionicWord = ""
                    val hLength:Int = ceil((wordLength/2).toDouble()).toInt()
                    val partToBold = word.substring(0, hLength+1)
                    partToBold.forEach { char ->
                        bionicWord += if(char.isLetterOrDigit()){
                            getBoldCharacter(char)
                        }else{
                            char
                        }
                    }
                    if(hLength+1<wordLength){
                       bionicWord += word.substring(hLength+1)
                    }
                    bionicWords.add(bionicWord)
                }
            }
            return bionicWords.joinToString(" ")
        }

        private fun changingCase(text: String): String {
            var res = ""
            var index = 0;
            text.forEach {
                if(it.isLetter()){
                    res += if(index%2==0){
                        it.uppercaseChar()
                    }else{
                        it.lowercaseChar()
                    }
                    index++
                }else{
                    res += it
                }
            }
            return res
        }

        fun translate(textToTranslate:String,targetLanguage: TargetLanguage) : String
        {
            var translated = ""
            when(targetLanguage){
                TargetLanguage.Reversed -> {
                    translated = reversed(textToTranslate)
                }
                TargetLanguage.ReversedWords -> {
                    translated = reversedWords(textToTranslate)
                }
                TargetLanguage.Acronyms -> {
                    translated = acronyms(textToTranslate)
                }
                TargetLanguage.Symbols -> {
                    translated = symbols(textToTranslate)
                }
                TargetLanguage.ChangingCase ->{
                    translated = changingCase(textToTranslate)
                }
                TargetLanguage.Highlighted -> {
                    translated = highlighted(textToTranslate)
                }
            }
            return translated
        }
    }
}
