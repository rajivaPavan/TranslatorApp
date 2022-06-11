package com.example.translatorapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class TranslatorScreenUIState(
    val showLanguages: Boolean = false
)


class TranslatorScreenViewModel(): ViewModel() {

    var uiState by mutableStateOf(TranslatorScreenUIState())
        private set
}