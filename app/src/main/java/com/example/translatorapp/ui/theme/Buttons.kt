package com.example.translatorapp.ui.theme

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BackButton(
    onClick: ()-> Unit,
    tint: Color = MaterialTheme.colors.onSecondary
){
    IconButton(onClick = onClick) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = "back",
            tint = tint
        )
    }
}

@Composable
fun ClearButton(
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colors.onSecondary
) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.Rounded.Clear,
            contentDescription = "back",
            tint = tint
        )
    }
}