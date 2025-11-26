package com.example.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.domain.entity.Comment
import com.example.domain.repository.PostRepository
import com.example.presentation.Route
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

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        savedStateHandle = SavedStateHandle()

        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<Route.Details>() } returns Route.Details(postId = 10)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads comments successfully`() = runTest {
        val fakeComments = listOf(
            Comment(1, 10, "A", "a@x.com", "Hi"),
            Comment(2, 10, "B", "b@x.com", "Hello")
        )

        coEvery { repository.getAllCommentsByPostId(10) } returns fakeComments

        val vm = DetailsViewModel(savedStateHandle, repository, testDispatcher)
        advanceUntilIdle()

        val state = vm.state.value
        assertThat(state.comments).isEqualTo(fakeComments)
        assertThat(state.error).isNull()
    }

    @Test
    fun `init handles error correctly`() = runTest {
        coEvery { repository.getAllCommentsByPostId(10) } throws RuntimeException("fail")

        val vm = DetailsViewModel(savedStateHandle, repository, testDispatcher)
        advanceUntilIdle()

        val state = vm.state.value
        assertThat(state.error).isNotNull()
    }

    @Test
    fun `retry loads comments`() = runTest {
        coEvery { repository.getAllCommentsByPostId(10) } throws RuntimeException("fail")

        val vm = DetailsViewModel(savedStateHandle, repository, testDispatcher)
        advanceUntilIdle()

        val fakeComments = listOf(Comment(1, 10, "User", "u@example.com", "Comment"))
        coEvery { repository.getAllCommentsByPostId(10) } returns fakeComments

        vm.onClickRetry()
        advanceUntilIdle()

        val state = vm.state.value
        assertThat(state.comments).isEqualTo(fakeComments)
        assertThat(state.error).isNull()
    }
}
