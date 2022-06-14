package com.example.translatorapp

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class TranslatorViewModel() :ViewModel(){

    private var _currentTargetLanguage: MutableLiveData<TargetLanguage> = MutableLiveData(
        defaultTargetLanguage)
    
    val currentTargetLanguage : LiveData<TargetLanguage> = _currentTargetLanguage

    fun setTargetLanguage(language: TargetLanguage) {
        _currentTargetLanguage.value = language
    }
}

@Composable
fun TranslatorApp(viewModel: TranslatorViewModel = viewModel()){

    val currentTargetLanguage by viewModel.currentTargetLanguage.observeAsState(
        defaultTargetLanguage)

    val navController = rememberNavController()
    
    TranslatorAppContent(
        currentTargetLanguage = currentTargetLanguage,
        setTargetLanguage = { viewModel.setTargetLanguage(it) },
        navController = navController,
    )
}

@Composable
fun TranslatorAppContent(
    currentTargetLanguage: TargetLanguage,
    setTargetLanguage: (TargetLanguage) -> Unit,
    navController: NavHostController,
){

    NavHost( navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ){
        composable(route = Screen.Home.route){
            HomeScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                targetLanguage = currentTargetLanguage,
                setTargetLanguage = setTargetLanguage,
                onTranslatorClick = { navController.navigate(Screen.Translation.route) }
            )
        }
        val onBackClick = {
            if(navController.previousBackStackEntry != null){
                navController.popBackStack()
            }
        }
        composable(route = Screen.Translation.route){
            TranslationScreen(
                onBackClick = onBackClick,
                targetLanguage = currentTargetLanguage,
            )
        }
        composable(route = Screen.Settings.route){
            TranslatorSettings(
                onBackClick = onBackClick
            )
        }
    }

}
