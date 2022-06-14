package com.example.translatorapp

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class HomeScreenViewModel(): ViewModel() {

    private var _isShowLanguages : MutableLiveData<Boolean> = MutableLiveData(false)
    val isShowLanguages: LiveData<Boolean> = _isShowLanguages

    fun showLanguages(isShow: Boolean){
        _isShowLanguages.value = isShow
    }
}

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    targetLanguage: TargetLanguage,
    setTargetLanguage: (TargetLanguage) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    onTranslatorClick: () -> Unit,
){

    val isShowLanguages : Boolean by
        homeScreenViewModel.isShowLanguages.observeAsState(false)

    HomeScreenContent(
        isShowLanguages = isShowLanguages,
        onSettingsClick = onSettingsClick,
        targetLanguage = targetLanguage,
        setTargetLanguage = setTargetLanguage,
        showLanguages = { homeScreenViewModel.showLanguages(it) },
        onTranslatorClick = onTranslatorClick
    )
}

@Composable
fun HomeScreenContent(
    isShowLanguages: Boolean,
    onSettingsClick: () -> Unit,
    targetLanguage: TargetLanguage,
    setTargetLanguage: (TargetLanguage) -> Unit,
    showLanguages: (Boolean) -> Unit,
    onTranslatorClick: () -> Unit
) {
    val transition = updateTransition(isShowLanguages, label = "")
    val bottomPanelHeight by transition.animateFloat(
        label = "Bottom Panel Height",
        transitionSpec = { tween(durationMillis = 300, easing = LinearOutSlowInEasing) }
    ) { increase -> if(increase) 0.4f else 0.2f }

    val headerWeight = 0.08f
    val middlePanelWeight = 1f - headerWeight - bottomPanelHeight

    Column(Modifier.background(color = MaterialTheme.colors.background)){

        Row(modifier = Modifier.weight(headerWeight)){
            TranslatorHeader(onSettingsClick = onSettingsClick)
        }
        Row(modifier = Modifier.weight(middlePanelWeight)){
            TranslatorPanel(onClick =  {
                showLanguages(false)
                onTranslatorClick()
            })
        }
        Row(modifier = Modifier.weight(bottomPanelHeight)
        ){
            LanguagesPanel(
                isShowLanguages = isShowLanguages,
                selectedLanguage = targetLanguage,
                setSelectedLanguage = setTargetLanguage,
                showLanguages = showLanguages
            )
        }
    }
}

@Composable
fun TranslatorHeader(onSettingsClick: ()-> Unit){
    Row(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.secondary)
        .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(0.85f, fill = true),
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
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Rounded.Settings,
                    contentDescription = "settings",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}

@Composable
fun TranslatorPanel(onClick: ()->Unit){
    Column(modifier = Modifier
        .background(
            color = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
        )
        .padding(8.dp)
        .clickable {
            onClick()
        }
    ){
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium,
            LocalContentColor provides MaterialTheme.colors.onSecondary
        ) {
            Text(
                text = "Enter text",
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}


@Composable
fun LanguagesPanel(
    isShowLanguages:Boolean,
    selectedLanguage: TargetLanguage,
    showLanguages: (Boolean)-> Unit,
    setSelectedLanguage: (TargetLanguage) -> Unit
){
    AnimatedVisibility(
        visible = !isShowLanguages,
        enter = fadeIn() + slideInVertically(initialOffsetY = {it})
    ) {
        SelectLanguage(selectedLanguage.name, onBtnClick = {showLanguages(true)})
    }
    AnimatedVisibility(
        visible = isShowLanguages,
        exit = fadeOut()+ slideOutHorizontally(targetOffsetX = {-it})
    ) {

        val backHandlingEnabled by remember { mutableStateOf(true) }
        BackHandler(backHandlingEnabled) {
            showLanguages(false)
        }

        LanguagesList(
            selectedLanguage = selectedLanguage,
            onBackPress = {showLanguages(false)},
            setTargetLanguage = setSelectedLanguage
        )
    }
}
