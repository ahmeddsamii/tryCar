package com.example.presentation.home

import com.example.domain.repository.PostRepository
import com.example.presentation.shared.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val postRepository: PostRepository
) : BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()), HomeInteractionListener {

    init {
        getAllPosts()
    }

    private fun getAllPosts() {
        tryToExecute(
            block = { postRepository.getAllPosts() },
            onSuccess = { updateState { copy(posts = it) } },
            onError = { updateState { copy(error = it) } },
            onStart = { updateState { copy(isLoading = true, error = null) } },
            onEnd = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onClickPost(postId: Int) {
        sendEffect(HomeUiEffect.DetailsNavigation(postId))
    }

    override fun onClickRetry() {
        updateState { copy(error = null) }
        getAllPosts()
    }
}