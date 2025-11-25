package com.example.data.remote.mapper

import com.example.data.remote.dto.CommentDto
import com.example.domain.entity.Comment

fun CommentDto.toEntity(): Comment {
    return Comment(
        postId = postId,
        id = id,
        name = name,
        email = email,
        body = body
    )
}

fun List<CommentDto>.toEntityList() = map { it.toEntity() }