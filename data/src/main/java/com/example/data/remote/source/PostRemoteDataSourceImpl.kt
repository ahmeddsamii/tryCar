package com.example.data.remote.source

import com.example.data.remote.dto.CommentDto
import com.example.data.remote.dto.PostDto
import com.example.data.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single(binds = [PostRemoteDataSource::class])
class PostRemoteDataSourceImpl(
    private val httpClient: HttpClient
): PostRemoteDataSource {
    override suspend fun getAllPosts(): List<PostDto> {
        return safeApiCall<List<PostDto>> {
            httpClient.get("/posts")
        }
    }

    override suspend fun getAllCommentsByPostId(postId: Int): List<CommentDto> {
        return safeApiCall<List<CommentDto>> {
            httpClient.get("/posts/$postId/comments")
        }
    }
}