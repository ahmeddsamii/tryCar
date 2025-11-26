package com.example.data.remote.mapper

import com.example.data.remote.dto.CommentDto
import com.example.data.util.orZero
import com.example.domain.entity.Comment

fun CommentDto.toEntity(): Comment {
    return Comment(
        postId = postId.orZero(),
        id = id.orZero(),
        name = name.orEmpty(),
        email = email.orEmpty(),
        body = body.orEmpty()
    )
}

fun List<CommentDto>.toEntityList() = map { it.toEntity() }