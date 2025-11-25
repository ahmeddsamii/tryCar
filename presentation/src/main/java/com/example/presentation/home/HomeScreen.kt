package com.example.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    Box(Modifier.fillMaxSize()){
        Text(text = state.posts.firstOrNull()?.id.toString(), modifier = Modifier.align(Alignment.Center))
    }
}