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
    private val connectivityObserver: ConnectivityObserver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()), HomeInteractionListener {

    private val isConnected = connectivityObserver.isConnected

    init {
        getAllPosts()
        syncPendingFavorites()
    }

    private fun syncPendingFavorites() {
        tryToExecute(
            block = {
                if (isConnected.first()) {
                    postRepository.getPendingFavoritePosts().forEach { pendingPost ->
                        addFavoriteToServer(pendingPost.id)
                        postRepository.markFavoriteAsSynced(pendingPost.id)
                    }
                }
            },
        )
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

                if (isConnected.first()) {
                    addFavoriteToServer(post.id)
                    postRepository.markFavoriteAsSynced(post.id)
                }
            },
            dispatcher = dispatcher
        )
    }

    private fun addFavoriteToServer(postId: Int) {
        Log.d("TAG", "🚀 addFavoriteToServer with id: $postId...")
    }
}