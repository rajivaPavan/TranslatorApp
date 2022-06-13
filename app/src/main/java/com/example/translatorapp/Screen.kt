package com.example.translatorapp

sealed class Screen(val route: String){
    object Home: Screen(route = "translator")
    object Translation: Screen(route = "translation")
    object Settings: Screen(route = "settings")
}
