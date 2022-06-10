package com.example.translatorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatorapp.ui.theme.TranslatorAppTheme


@Composable
fun TranslatorScreen(){

    val headerWeight = 0.08f
    val bottomPanelWeight = 0.2f
    val middlePanelWeight = 1f - headerWeight - bottomPanelWeight

    Column(Modifier.background(color = MaterialTheme.colors.background)){
        //header
        Row(modifier = Modifier.weight(headerWeight)){
            TranslatorHeader()
        }
        Row(modifier = Modifier.weight(middlePanelWeight)){
            MainTranslatorPanel()
        }
        Row(modifier = Modifier.weight(bottomPanelWeight)){
            TranslatePanel()
        }
    }
}

@Composable
fun TranslatorHeader(){
    Row(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Translator",
                fontFamily = FontFamily.SansSerif,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.contentColorFor(
                    backgroundColor = MaterialTheme.colors.secondary)
            )
        }
        Column(modifier = Modifier.weight(0.15f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.Settings,
                    contentDescription = "settings",
                    tint = MaterialTheme.colors.contentColorFor(
                        backgroundColor = MaterialTheme.colors.secondary)
                )
            }
        }
    }
}

@Composable
fun MainTranslatorPanel(){
    Column(modifier = Modifier
        .background(
            color = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
        )
        .padding(8.dp, 8.dp, 8.dp, 8.dp)){
        var textState by rememberSaveable { mutableStateOf("") }
        TextField(value = textState,
            onValueChange = { textState = it },
            placeholder = {
                Text(text = "Enter text",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.weight(FontWeight.Medium.weight.toFloat()))
            },
            modifier = Modifier.fillMaxSize(),
            textStyle = MaterialTheme.typography.h5,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
    }
}

@Composable
fun TranslatePanel(){
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 0.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text("Translate to",
            style = MaterialTheme.typography.h5,
            color =  MaterialTheme.colors.contentColorFor(
                backgroundColor = MaterialTheme.colors.secondary))
        Button(onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(10.dp),
            elevation = null
        ) {
            Text(text="Language", color = Color.Black,
                modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TranslatorAppTheme() {
        TranslatorScreen()
    }
}