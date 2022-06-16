package com.example.translatorapp

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.translatorapp.ui.theme.BackButton


@Composable
fun TranslatorSettings(onBackClick: () -> Unit){
    Column(
        Modifier
            .background(color = MaterialTheme.colors.secondary)
            .fillMaxSize()
    ){
        val rowModifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
        Row(modifier = rowModifier,
        ){
            BackButton(onClick = onBackClick)
        }
        var tapToTranslate by rememberSaveable { mutableStateOf(false)}
        val switch = {tapToTranslate=!tapToTranslate}
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colors.onSecondary) {
            Row(modifier = rowModifier
                .height(IntrinsicSize.Min)
                .clickable(onClick = { switch() }),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(16.dp)
                ) {

                    Text(text ="Use Tap to Translate",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(text = "When on, Tap to Translate runs in the background",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(0.2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Switch(checked = tapToTranslate,
                        onCheckedChange = {tapToTranslate = if(!(it&&tapToTranslate)) it else tapToTranslate },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.primaryVariant,
                            checkedTrackColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }

    }
}