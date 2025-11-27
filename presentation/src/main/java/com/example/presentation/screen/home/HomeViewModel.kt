package com.example.presentation.screen.home

import android.util.Log
import com.example.domain.entity.Post
import com.example.domain.repository.PostRepository
import com.example.domain.util.ConnectivityObserver
import com.example.presentation.shared.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val postRepository: PostRepository,
    connectivityObserver: ConnectivityObserver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()), HomeInteractionListener {

    private val isConnected = connectivityObserver.isConnected

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
            dispatcher = dispatcher
        )
    }

    override fun onClickPost(postId: Int) {
        sendEffect(HomeUiEffect.DetailsNavigation(postId))
    }

    override fun onClickRetry() {
        updateState { copy(error = null) }
        getAllPosts()
    }

    override fun onClickFavorite(post: Post) {
        tryToExecute(
            block = {
                postRepository.insertFavoritePost(post)
                if (isConnected.first()) addFavoriteToServer()
            }
        )
    }

    private fun addFavoriteToServer() {
        Log.d("TAG", "addFavoriteToServer...")
    }
}