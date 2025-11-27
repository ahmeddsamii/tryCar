package com.example.presentation.home

import app.cash.turbine.test
import com.example.domain.entity.Post
import com.example.domain.repository.PostRepository
import com.example.domain.util.ConnectivityObserver
import com.example.presentation.screen.home.HomeUiEffect
import com.example.presentation.screen.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        connectivityObserver = mockk(relaxed = true)
        viewModel = HomeViewModel(repository, connectivityObserver, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init handles error correctly`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getAllPosts() } throws exception

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.error).isNotNull()
    }

    @Test
    fun `onClickRetry calls getAllPosts again and updates state`() = runTest {
        // Given
        val fakePosts = listOf(Post(1, 1, "X", "Y"))
        coEvery { repository.getAllPosts() } returns fakePosts

        // When
        viewModel.onClickRetry()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.posts).isEqualTo(fakePosts)
    }

    @Test
    fun `onClickPost emits navigation effect with correct id`() = runTest {
        // Given
        coEvery { repository.getAllPosts() } returns emptyList()

        // When
        advanceUntilIdle()

        // Then
        viewModel.effect.test {
            viewModel.onClickPost(42)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeUiEffect.DetailsNavigation::class.java)
            assertThat((effect as HomeUiEffect.DetailsNavigation).postId).isEqualTo(42)
        }
    }

    @Test
    fun `onClickFavorite calls insertFavoritePost`() = runTest {
        // Given
        val fakePost = Post(1, 10, "Title", "Body")
        coEvery { connectivityObserver.isConnected } returns flowOf(true)

        // When
        viewModel.onClickFavorite(fakePost)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { repository.insertFavoritePost(fakePost) }
    }

    @Test
    fun `onClickFavorite when connected triggers insertFavoritePost`() = runTest {
        // Given
        val post = Post(1, 123, "T", "B")
        coEvery { connectivityObserver.isConnected } returns flowOf(true)

        // When
        viewModel.onClickFavorite(post)
        advanceUntilIdle()

        // Then
        coVerify { repository.insertFavoritePost(post) }
    }

    @Test
    fun `onClickFavorite when NOT connected still inserts favorite`() = runTest {
        // Given
        val post = Post(1, 123, "T", "B")
        coEvery { connectivityObserver.isConnected } returns flowOf(false)

        // When
        viewModel.onClickFavorite(post)
        advanceUntilIdle()

        // Then
        coVerify { repository.insertFavoritePost(post) }
    }
}
