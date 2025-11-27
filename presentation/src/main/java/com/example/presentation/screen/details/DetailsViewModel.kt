package com.example.presentation.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.domain.repository.PostRepository
import com.example.presentation.navigation.Route
import com.example.presentation.shared.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<DetailsUiState, DetailsUiEffect>(DetailsUiState()), DetailsInteractionListener {

    private val postId = savedStateHandle.toRoute<Route.Details>().postId

    init {
        getAllCommentsByPostId()
    }

    private fun getAllCommentsByPostId() {
        tryToExecute(
            block = { postRepository.getAllCommentsByPostId(postId) },
            onSuccess = { updateState { copy(comments = it) } },
            onError = { updateState { copy(error = it) } },
            onStart = { updateState { copy(isLoading = true) } },
            onEnd = { updateState { copy(isLoading = false) } },
            dispatcher = dispatcher
        )
    }

    override fun onClickRetry() {
        updateState { copy(error = null) }
        getAllCommentsByPostId()
    }
}