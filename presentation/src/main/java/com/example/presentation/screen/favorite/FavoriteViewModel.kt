package com.example.presentation.screen.favorite

import com.example.domain.repository.PostRepository
import com.example.presentation.shared.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FavoriteViewModel(
    private val postRepository: PostRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<FavoriteUiState, FavoriteUiEffect>(FavoriteUiState()),
    FavoriteInteractionListener {

    init {
        getAllFavoritePosts()
    }

    private fun getAllFavoritePosts() {
        tryToExecute(
            block = { postRepository.getAllFavoritePosts() },
            onSuccess = { updateState { copy(posts = it) } },
            onError = { updateState { copy(error = it) } },
            onStart = { updateState { copy(isLoading = true) } },
            onEnd = { updateState { copy(isLoading = false) } },
            dispatcher = dispatcher,
        )
    }

    override fun onClickBookmark(postId: Int) {
        tryToExecute(
            block = { postRepository.deletePostById(postId) },
            onSuccess = { getAllFavoritePosts() },
            onError = { updateState { copy(error = it) } },
            dispatcher = dispatcher
        )
    }
}