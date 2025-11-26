package com.example.presentation.home

import com.example.domain.entity.Post
import com.example.domain.repository.PostRepository
import com.example.domain.util.ConnectivityObserver
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
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PostRepository
    private lateinit var connectivityObserver: ConnectivityObserver

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        connectivityObserver = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads posts successfully`() = runTest {
        val fakePosts = listOf(
            Post(1, 1, "A", "B"),
            Post(2, 2, "B", "C")
        )
        coEvery { repository.getAllPosts() } returns fakePosts

        val viewModel = HomeViewModel(repository, connectivityObserver, testDispatcher)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.posts).isEqualTo(fakePosts)
    }

    @Test
    fun `init handles error correctly`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { repository.getAllPosts() } throws exception

        val viewModel = HomeViewModel(repository, connectivityObserver, testDispatcher)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.error).isNotNull()
    }

    @Test
    fun `onClickRetry calls getAllPosts again and updates state`() = runTest {
        coEvery { repository.getAllPosts() } throws RuntimeException("fail")

        val viewModel = HomeViewModel(repository, connectivityObserver, testDispatcher)
        advanceUntilIdle()

        val fakePosts = listOf(Post(1, 1, "X", "Y"))
        coEvery { repository.getAllPosts() } returns fakePosts

        viewModel.onClickRetry()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.posts).isEqualTo(fakePosts)
    }
}
