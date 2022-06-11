package com.example.translatorapp

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import kotlinx.coroutines.delay
import java.util.concurrent.LinkedBlockingQueue


@Composable
fun TranslatorScreen(){

    val headerWeight = 0.08f
    var isShowLanguages by rememberSaveable { mutableStateOf(false)}

    var transition = updateTransition(isShowLanguages)

    val changingHeight: Float by animateFloatAsState(
        targetValue = if (isShowLanguages) 0.4f else 0.2f,
        animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
    )

    val bottomPanelHeight by transition.animateFloat(label = "Bottom Panel Height") { increase ->
        if(increase) 0.4f else 0.2f
    }

    val middlePanelWeight = 1f - headerWeight - changingHeight

    fun showLanguages(isShow: Boolean){
        isShowLanguages = isShow
    }

    @Composable
    fun LanguagesPanel(){
        AnimatedVisibility(
            visible = !isShowLanguages,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column (modifier = Modifier
                .fillMaxSize()
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
        AnimatedVisibility(
            visible = isShowLanguages,
        ) {
            Column(modifier = Modifier
                .fillMaxSize()){
                IconButton(onClick = { showLanguages(false) }) {
                    Icon(Icons.Rounded.ArrowBack,
                        contentDescription = "back",
                        tint = MaterialTheme.colors.contentColorFor(
                            backgroundColor = MaterialTheme.colors.secondary))
                }
                val modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                Text("Translate to",
                    modifier = modifier,
                    style = MaterialTheme.typography.h5,
                    color =  MaterialTheme.colors.contentColorFor(
                        backgroundColor = MaterialTheme.colors.secondary))

                Column(modifier = modifier) {
                    languages.forEach{ language ->
                        TranslationLanguage(language)
                    }
                }


            }
        }
    }

    Column(Modifier.background(color = MaterialTheme.colors.background)){
        //header
        Row(modifier = Modifier.weight(headerWeight)){
            TranslatorHeader()
        }
        Row(modifier = Modifier.weight(middlePanelWeight)){
            MainTranslatorPanel()
        }
        Row(modifier = Modifier
            .weight(changingHeight)
        ){
            LanguagesPanel()
        }
    }


}

@Composable
fun TranslatorHeader(){
    Row(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.secondary)
        .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Translator",
                fontFamily = FontFamily.SansSerif,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSecondary
            )
        }
        Column(modifier = Modifier.weight(0.15f),
            horizontalAlignment = Alignment.End) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.Settings,
                    contentDescription = "settings",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}

@Composable
fun MainTranslatorPanel(){
    Box(modifier = Modifier
        .background(
            color = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
        )
        .padding(8.dp)){
        var textState by rememberSaveable { mutableStateOf("") }
        TextField(value = textState,
            onValueChange = { textState = it },
            placeholder = {
                Text(text = "Enter text",
                    style = MaterialTheme.typography.h4)
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
fun TranslationLanguage(language: CustomLanguage){
    val rowColor = if(language.isSelected)
        MaterialTheme.colors.primary else MaterialTheme.colors.background
    Row(
        modifier = Modifier.background(color = rowColor,
                shape = RoundedCornerShape(16.dp)),

    ){
        Text(text = language.name,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.contentColorFor(backgroundColor = rowColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }

}

data class CustomLanguage(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false
)

val languages = listOf<CustomLanguage>(
    CustomLanguage(0, "#language1",true),
    CustomLanguage(1, "#langauge2"),
    CustomLanguage(2, "#langauge3"),
    CustomLanguage(3, "#langauge4")
)


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TranslatorAppTheme() {
        TranslatorScreen()
    }
}