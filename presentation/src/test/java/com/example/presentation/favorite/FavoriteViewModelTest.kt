package com.example.presentation.favorite

import com.example.domain.entity.Post
import com.example.domain.repository.PostRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
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
class FavoriteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PostRepository
    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads favorite posts successfully`() = runTest {
        val fakePosts = listOf(
            Post(1, 1, "A", "Body A"),
            Post(1, 2, "B", "Body B")
        )

        // Given
        coEvery { repository.getAllFavoritePosts() } returns fakePosts

        // When
        viewModel = FavoriteViewModel(repository, testDispatcher)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.posts).isEqualTo(fakePosts)
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `init handles error while loading favorites`() = runTest {
        // Given
        coEvery { repository.getAllFavoritePosts() } throws RuntimeException("fail")

        // When
        viewModel = FavoriteViewModel(repository, testDispatcher)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.error).isNotNull()
        assertThat(state.posts).isEmpty()
    }

    @Test
    fun `onClickBookmark handles delete failure gracefully`() = runTest {
        coEvery { repository.getAllFavoritePosts() } returns emptyList()

        viewModel = FavoriteViewModel(repository, testDispatcher)
        coEvery { repository.deletePostById(any()) } throws RuntimeException("delete fail")

        // When
        viewModel.onClickBookmark(10)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.error).isNotNull()
    }
}
