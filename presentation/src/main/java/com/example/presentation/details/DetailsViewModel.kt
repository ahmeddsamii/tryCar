package com.example.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.domain.repository.PostRepository
import com.example.presentation.Route
import com.example.presentation.shared.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : BaseViewModel<DetailsUiState, DetailsUiEffect>(DetailsUiState()), CommentInteractionListener {

    private val postId = savedStateHandle.toRoute<Route.Details>().postId

    init {
        getAllCommentsByPostId()
    }

    private fun getAllCommentsByPostId() {
        tryToExecute(
            block = {postRepository.getAllCommentsByPostId(postId)},
            onSuccess = { updateState { copy(comments = it) } },
            onError = { updateState { copy(error = it) } },
            onStart = {updateState { copy(isLoading = true) } },
            onEnd = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onClickRetry() {
        updateState { copy(error = null) }
        getAllCommentsByPostId()
    }
}