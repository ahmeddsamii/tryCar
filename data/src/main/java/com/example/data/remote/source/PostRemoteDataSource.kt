package com.example.data.remote.source

import com.example.data.remote.dto.CommentDto
import com.example.data.remote.dto.PostDto

interface PostRemoteDataSource {
    suspend fun getAllPosts() : List<PostDto>
    suspend fun getAllCommentsByPostId(postId: Int) : List<CommentDto>
}