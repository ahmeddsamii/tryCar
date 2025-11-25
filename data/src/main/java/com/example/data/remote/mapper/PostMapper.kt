package com.example.data.remote.mapper

import com.example.data.remote.dto.PostDto
import com.example.domain.entity.Post

fun PostDto.toEntity(): Post {
    return Post(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}

fun List<PostDto>.toEntityList(): List<Post> = this.map { it.toEntity() }
