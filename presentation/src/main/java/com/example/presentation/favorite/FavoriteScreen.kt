package com.example.presentation.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FavoriteScreen() {
    Box(Modifier.fillMaxSize()) {
        Text(text = "Favorite", modifier = Modifier.align(Alignment.Center))
    }
}