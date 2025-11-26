package com.example.data.repository

import com.example.data.local.entity.LocalPost
import com.example.data.local.source.PostLocalDataSource
import com.example.data.remote.dto.CommentDto
import com.example.data.remote.dto.PostDto
import com.example.data.remote.source.PostRemoteDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostRepositoryImplTest {

    private lateinit var remoteDataSource: PostRemoteDataSource
    private lateinit var localDataSource: PostLocalDataSource
    private lateinit var repository: PostRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        repository = PostRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getAllPosts returns remote posts when remote succeeds`() = runTest {
        // Given
        val remotePosts = listOf(
            PostDto(userId = 1, id = 1, title = "Post 1", body = "Body 1")
        )
        coEvery { remoteDataSource.getAllPosts() } returns remotePosts

        // When
        val result = repository.getAllPosts()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Post 1")
    }

    @Test
    fun `getAllPosts calls remote data source`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllPosts() } returns emptyList()

        // When
        repository.getAllPosts()

        // Then
        coVerify(exactly = 1) { remoteDataSource.getAllPosts() }
    }

    @Test
    fun `getAllPosts clears local cache when remote succeeds`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllPosts() } returns emptyList()

        // When
        repository.getAllPosts()

        // Then
        verify(exactly = 1) { localDataSource.clearAllPosts() }
    }

    @Test
    fun `getAllPosts saves remote posts to local when remote succeeds`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllPosts() } returns emptyList()

        // When
        repository.getAllPosts()

        // Then
        verify(exactly = 1) { localDataSource.insertAllPosts(any()) }
    }

    @Test
    fun `getAllPosts returns local posts when remote fails`() = runTest {
        // Given
        val localPosts = listOf(
            LocalPost(userId = 1, id = 1, title = "Cached Post", body = "Cached Body")
        )
        coEvery { remoteDataSource.getAllPosts() } throws Exception("Network error")
        coEvery { localDataSource.getAllPosts() } returns localPosts

        // When
        val result = repository.getAllPosts()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Cached Post")
    }

    @Test
    fun `getAllPosts does not clear cache when remote fails`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllPosts() } throws Exception("Network error")
        coEvery { localDataSource.getAllPosts() } returns emptyList()

        // When
        repository.getAllPosts()

        // Then
        verify(exactly = 0) { localDataSource.clearAllPosts() }
    }

    @Test
    fun `getAllPosts returns empty list when both sources fail`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllPosts() } throws Exception("Network error")
        coEvery { localDataSource.getAllPosts() } returns emptyList()

        // When
        val result = repository.getAllPosts()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllCommentsByPostId returns comments from remote`() = runTest {
        // Given
        val comments = listOf(
            CommentDto(postId = 1, id = 1, name = "Comment", email = "user@test.com", body = "Body")
        )
        coEvery { remoteDataSource.getAllCommentsByPostId(1) } returns comments

        // When
        val result = repository.getAllCommentsByPostId(1)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Comment")
    }

    @Test
    fun `getAllCommentsByPostId calls remote with correct postId`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllCommentsByPostId(any()) } returns emptyList()

        // When
        repository.getAllCommentsByPostId(5)

        // Then
        coVerify(exactly = 1) { remoteDataSource.getAllCommentsByPostId(5) }
    }

    @Test
    fun `getAllCommentsByPostId returns empty list when no comments`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllCommentsByPostId(any()) } returns emptyList()

        // When
        val result = repository.getAllCommentsByPostId(1)

        // Then
        assertThat(result).isEmpty()
    }
}