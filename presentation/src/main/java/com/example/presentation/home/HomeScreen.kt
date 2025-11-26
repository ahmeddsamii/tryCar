package com.example.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.Route
import com.example.presentation.shared.ErrorState
import com.example.presentation.shared.NoConnection
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreenContent(state, viewModel)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeUiEffect.DetailsNavigation ->
                    navController.navigate(Route.Details(effect.postId))
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    listener: HomeInteractionListener
) {

    if (state.error == ErrorState.NoInternet && state.posts.isEmpty()) {
        NoConnection(onClickRetry = { listener.onClickRetry() })
    } else
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.posts) { post ->
                PostItem(
                    title = post.title,
                    body = post.body,
                    onClickSave = { listener.onClickFavorite(post) },
                    onClick = { listener.onClickPost(post.id) })
            }
        }
}

@Composable
private fun PostItem(
    title: String,
    body: String,
    onClick: () -> Unit,
    onClickSave: () -> Unit,
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
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_bookmark),
                contentDescription = "bookmark icon",
                modifier = Modifier
                    .width(20.dp)
                    .clickable { onClickSave() },
            )
        }
        Text(text = body)
    }
}