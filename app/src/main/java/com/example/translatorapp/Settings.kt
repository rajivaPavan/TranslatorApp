package com.example.translatorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.translatorapp.ui.theme.BackButton
import com.example.translatorapp.ui.theme.TranslatorAppTheme

@Composable
fun TranslatorSettings(onBackClick: () -> Unit){
    Column(Modifier.background(color = MaterialTheme.colors.background)){
        Row(){
            BackButton(onClick = onBackClick)
        }
    }
}