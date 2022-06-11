package com.example.translatorapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatorapp.ui.theme.TranslatorAppTheme


@Composable
fun TranslatorScreen(){

    val headerWeight = 0.08f
    val initialBottomPanelWeight = 0.2f
    var bottomPanelWeight by rememberSaveable { mutableStateOf(initialBottomPanelWeight)}
    var middlePanelWeight = 1f - headerWeight - bottomPanelWeight
    var isShowLanguages by rememberSaveable { mutableStateOf(false)}
    
    fun showLanguages(isShow: Boolean){
        isShowLanguages = isShow;
        if(isShow){
            bottomPanelWeight *= 2
        }else{
            bottomPanelWeight = initialBottomPanelWeight
        }
    }

    @Composable
    fun MainTranslatorPanel(heightFill: Float){
        Row(modifier = Modifier
            .background(
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
            ).animateContentSize()
            .fillMaxWidth()
            .fillMaxHeight(heightFill)
            .padding(8.dp, 8.dp, 8.dp, 8.dp)
        ){
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
        AnimatedVisibility(visible = !isShowLanguages) {
            Column (modifier = Modifier
                .fillMaxSize().animateContentSize()
                .padding(0.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally){
                Text("Translate to",
                    style = MaterialTheme.typography.h5,
                    color =  MaterialTheme.colors.contentColorFor(
                        backgroundColor = MaterialTheme.colors.secondary))
                Button(onClick = { showLanguages(true) },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null
                ) {
                    Text(text="Language", color = Color.Black,
                        modifier = Modifier.padding(8.dp))
                }
            }
        }
        AnimatedVisibility(visible = isShowLanguages) {
            Column(modifier = Modifier
                .fillMaxSize().animateContentSize()
                ) {
                IconButton(onClick = { showLanguages(false) }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "back",
                        tint = MaterialTheme.colors.contentColorFor(
                            backgroundColor = MaterialTheme.colors.secondary)
                    )

                }
                Text("Translate to",
                    modifier = Modifier.padding(16.dp, 8.dp),
                    style = MaterialTheme.typography.h5,
                    color =  MaterialTheme.colors.contentColorFor(
                        backgroundColor = MaterialTheme.colors.secondary))

            }
        }
    }



    Column(Modifier.background(color = MaterialTheme.colors.background)){
        //header
        TranslatorHeader(headerWeight)
        MainTranslatorPanel(middlePanelWeight)
        TranslatePanel()
    }
}

@Composable
fun TranslatorHeader(heightFill:Float){
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(heightFill)
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TranslatorAppTheme() {
        TranslatorScreen()
    }
}