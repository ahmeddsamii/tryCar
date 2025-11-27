package com.example.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.domain.entity.Comment
import com.example.domain.repository.PostRepository
import com.example.presentation.navigation.Route
import com.example.presentation.screen.details.DetailsViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PostRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        savedStateHandle = SavedStateHandle()

        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<Route.Details>() } returns Route.Details(postId = 10)

        viewModel = DetailsViewModel(savedStateHandle, repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init handles error correctly`() = runTest {
        // Given
        coEvery { repository.getAllCommentsByPostId(10) } throws RuntimeException("fail")

        // When & Then
        val state = viewModel.state.value
        assertThat(state.error).isNotNull()
    }

    @Test
    fun `retry loads comments`() = runTest {
        // Given
        val fakeComments = listOf(Comment(1, 10, "User", "u@example.com", "Comment"))
        coEvery { repository.getAllCommentsByPostId(10) } returns fakeComments

        // When
        viewModel.onClickRetry()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.comments).isEqualTo(fakeComments)
        assertThat(state.error).isNull()
    }
}
