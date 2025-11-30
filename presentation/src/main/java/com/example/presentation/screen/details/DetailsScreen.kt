package com.example.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.navigation.LocalNavController
import com.example.presentation.shared.base.ErrorState
import com.example.presentation.shared.component.NoConnection
import com.example.presentation.shared.component.TopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(viewModel: DetailsViewModel = koinViewModel()) {

    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetailsScreenContent(state, viewModel)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailsUiEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }
}

@Composable
private fun DetailsScreenContent(
    state: DetailsUiState,
    listener: DetailsInteractionListener
) {

    when {
        state.error == ErrorState.NoInternet && state.comments.isEmpty() -> NoConnection(
            onClickRetry = { listener.onClickRetry() }
        )

        state.isLoading -> {
            CircularProgressIndicator(
                modifier = Modifier.requiredSize(32.dp)
            )
        }

        else -> CommentsList(state = state, listener = listener)
    }
}

@Composable
private fun CommentsList(
    state: DetailsUiState,
    listener: DetailsInteractionListener
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            TopAppBar(
                title = "Comments",
                painter = painterResource(R.drawable.ic_arrow_left),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                onLeadingIconClick = listener::onClickBack,
            )
        }

        items(state.comments) { comment ->
            PostItem(
                email = comment.email,
                body = comment.body,
            )
        }
    }
}

@Composable
private fun PostItem(
    email: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(100.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = email,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(text = body)
    }
}