package com.example.presentation.screen.favorite

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.shared.component.TopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    FavoriteScreenContent(state, viewModel)
}

@Composable
private fun FavoriteScreenContent(
    state: FavoriteUiState,
    listener: FavoriteInteractionListener
) {

    when {
        state.isLoading -> {
            CircularProgressIndicator(
                modifier = Modifier.requiredSize(32.dp)
            )
        }

        state.posts.isEmpty() -> {
            EmptyTrends()
        }

        else -> FavoritePostsList(state = state, listener = listener)
    }
}

@Composable
fun FavoritePostsList(
    state: FavoriteUiState,
    listener: FavoriteInteractionListener
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            TopAppBar(
                title = "Favorite Posts",
                painter = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
        }

        items(state.posts) { post ->
            PostItem(
                title = post.title,
                body = post.body,
                onClickSave = { listener.onClickBookmark(post.id) },
            )
        }
    }
}

@Composable
private fun PostItem(
    title: String,
    body: String,
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
                painter = painterResource(R.drawable.ic_card_bookmark_filled),
                contentDescription = "bookmark icon",
                modifier = Modifier
                    .width(20.dp)
                    .clickable { onClickSave() },
            )
        }
        Text(text = body)
    }
}

@Composable
private fun EmptyTrends(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(R.drawable.ic_empty_state),
            contentDescription = "No Favorite yet!",
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 12.dp)
        )

        Text(
            text = "No Favorite yet!",
            textAlign = TextAlign.Center,
            color = Color.Black,
        )
    }
}