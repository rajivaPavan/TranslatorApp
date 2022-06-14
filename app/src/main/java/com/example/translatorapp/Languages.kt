package com.example.translatorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.translatorapp.ui.theme.BackButton


val targetLanguages = listOf(
    TargetLanguage.Reversed,
    TargetLanguage.ReversedWords,
    TargetLanguage.Acronyms,
    TargetLanguage.Symbols,
    TargetLanguage.ChangingCase,
    TargetLanguage.Highlighted
)

//data class TargetLanguage(
//    val id: Int,
//    val name: String
//)
//
//val targetLanguages = listOf(
//    TargetLanguage(0, "#language1"),
//    TargetLanguage(1, "#langauge2"),
//    TargetLanguage(2, "#langauge3"),
//    TargetLanguage(3, "#langauge4")
//)

val defaultTargetLanguage = targetLanguages[0]

@Composable
fun SelectLanguage(
    selectedLanguage:String,
    onBtnClick: () -> Unit
){
    Column (
        modifier = Modifier
        .fillMaxSize()
        .padding(0.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Translate to",
            style = MaterialTheme.typography.h6,
            color =  MaterialTheme.colors.onSecondary
        )
        SelectedLanguageButton(selectedLanguage, onBtnClick)
    }
}

@Composable
fun SelectedLanguageButton(
    selectedLanguage: String,
    onBtnClick: ()->Unit
){
    Button(
        onClick = onBtnClick ,
        modifier = Modifier.fillMaxWidth(0.8f),
        shape = RoundedCornerShape(20.dp),
        elevation = null
    ) {
        Text(
            text=selectedLanguage,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun TranslationLanguage(
    isSelected: Boolean,
    language: TargetLanguage,
    onClick: ()-> Unit
){

    val rowColor = if(isSelected)
        MaterialTheme.colors.primary else MaterialTheme.colors.background

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                color = rowColor,
                shape = RoundedCornerShape(16.dp)
            )
    ){
        Text(
            text = language.name,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.contentColorFor(backgroundColor = rowColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
fun LanguagesList(selectedLanguage: TargetLanguage,
                  onBackPress: ()-> Unit,
                  setTargetLanguage: (TargetLanguage)->Unit){
    Column(modifier = Modifier
        .fillMaxSize()){

        BackButton(onBackPress)

        val modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        Text("Translate to",
            modifier = modifier,
            style = MaterialTheme.typography.h5,
            color =  MaterialTheme.colors.contentColorFor(
                backgroundColor = MaterialTheme.colors.secondary))

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            targetLanguages.forEach{ language ->
                val selected = language == selectedLanguage
                TranslationLanguage(selected,language,
                    onClick =  {
                        setTargetLanguage(language)
                        onBackPress()
                    }
                )
            }
        }
    }
}